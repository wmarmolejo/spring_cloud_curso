package com.kalettre.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.kalettre.springboot.app.commons.usuarios.models.entity.Usuario;
import com.kalettre.springboot.app.oauth.services.IUsuarioService;

import feign.FeignException;

@Component
public class AuthenticatioSuccessErrorHandler implements AuthenticationEventPublisher{

	private Logger log=LoggerFactory.getLogger(AuthenticatioSuccessErrorHandler.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails user=(UserDetails) authentication.getPrincipal();
		log.info("Success Login: "+user.getUsername());
		Usuario usuario=usuarioService.findByUsername(authentication.getName());
		if(usuario.getIntentos()!=null && usuario.getIntentos()>0) {
			usuario.setIntentos(0);	
			usuarioService.update(usuario, usuario.getId());
		}
		
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		log.error("Error en el login: "+exception.getMessage());
		try {
			Usuario usuario=usuarioService.findByUsername(authentication.getName());
			if(usuario.getIntentos()==null) {
				usuario.setIntentos(0);				
			}
			log.info("Intentos actuales es de: "+usuario.getIntentos());
			usuario.setIntentos(usuario.getIntentos()+1);
			log.info("Intentos despues es de: "+usuario.getIntentos());
			
			if(usuario.getIntentos()>=3) {
				log.error(String.format("El usuario %s des-habilitado por máximos intentos."+usuario.getUsername()));
				usuario.setEnabled(false);
			}
			usuarioService.update(usuario, usuario.getId());
			
		} catch (FeignException e) {
			log.error("Usuario %s no existe en el sistema",authentication.getName());
		}
		
		
		
	}

}
