package com.kalettre.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.kalettre.springboot.app.commons.usuarios.models.entity.Usuario;
import com.kalettre.springboot.app.oauth.clients.UsuarioFeignClient;

import brave.Tracer;
import feign.FeignException;

@Service //Clase la cual consulta el usuario para la autenticacion 
public class UsuarioService implements UserDetailsService, IUsuarioService{

	private Logger log = LoggerFactory.getLogger(UsuarioService.class);
	@Autowired
	private UsuarioFeignClient client;
	@Autowired
	private Tracer tracer;//traza para zipkin
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		try {					
		Usuario usuario =client.findByUsername(username);		
		List<GrantedAuthority> authorities=usuario.getRoles().
				stream().
				map(role->new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority->log.info("Role: "))
				.collect(Collectors.toList());
		log.info("Usuario autenticado: "+username);
		return new User(usuario.getUsername(),usuario.getPassword(),usuario.getEnabled(),true,
				true,true,authorities);
		} catch (FeignException e) {
			String errorMen="Error en el login, no existe el usuario '"+username+"' en el sistema";
			log.error(errorMen);
			tracer.currentSpan().tag("error.mensaje", errorMen+" : "+e.getMessage());//traza para zipkin
			throw new UsernameNotFoundException("Error en el login, no existe el usuario '"+username+"' en el sistema");			
		} 
		
	}

	@Override
	public Usuario findByUsername(String username) {	 
		return client.findByUsername(username);
	}

	@Override
	public Usuario update(Usuario usuario, Long id) {
		return client.update(usuario, id);
	}

}
