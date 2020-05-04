package com.kalettre.springboot.app.zuul.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter{

	@Override //configuracion token en zuul
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());

	}

	@Override //configuracion rutas
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
		.antMatchers(HttpMethod.GET,"/api/productos/listar","/api/items/listar","/api/usuarios/usuarios").permitAll()
		.antMatchers(HttpMethod.GET,
				"/api/productos/ver/{id}",
				"/api/items/ver/{id}/cantidad/{cantidad}",
				"/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.POST,"/api/productos/crear","/api/items/crear","/api/usuarios/crear").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT,"/api/productos/editar/{id}","/api/items/editar/{id}","/api/usuarios/usuarios/{id}").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE,"/api/productos/eliminar/{id}","/api/items/eliminar/{id}","/api/usuarios/usuarios/{id}").hasRole("ADMIN")
		.anyRequest().authenticated();//cualquier ruta que no halla sido configurada necesita autenticación

	}

	@Bean
	public JwtTokenStore tokenStore() {	
		return new JwtTokenStore(accessTokenConverter()); //captura en JwtAccessTokenConverter
	}

	@Bean //genera el JwtAccessTokenConverter con una clave
	public JwtAccessTokenConverter accessTokenConverter() { 
		JwtAccessTokenConverter tokenConverter=new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("algun_codigo_secreto_aeiou");
		return tokenConverter;
	}
	
}
