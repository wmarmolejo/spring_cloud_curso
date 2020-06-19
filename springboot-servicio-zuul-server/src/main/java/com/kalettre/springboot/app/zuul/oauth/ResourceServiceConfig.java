package com.kalettre.springboot.app.zuul.oauth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter{

	@Value("${config.security.oauth.jwt.key}")
	private String jwtk;
	
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
		.anyRequest().authenticated()
		.and().cors().configurationSource(corsConfigurationSource());//cualquier ruta que no halla sido configurada necesita autenticación //(cross-origin)

	}

	@Bean //(cross-origin)
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig=new CorsConfiguration();
		//corsConfig.setAllowedOrigins(Arrays.asList("*","localhost:4200")); //lista de los cliente que son permitidos
		//localhost:4200 --para que el unico cliente a acceder sea angular
		corsConfig.addAllowedOrigin("*");
		corsConfig.setAllowedMethods(Arrays.asList("POST","GET","PUT","DELETE","OPTIONS"));//permisos de metodos
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization","Content-Type")); //permisos a cabecera 
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig); //ruta a la cual permite
		return source;
	}
	
	@Bean //Filtro para que se configure en toda la aplicación de forma global, (cross-origin)
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean=new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public JwtTokenStore tokenStore() {	
		return new JwtTokenStore(accessTokenConverter()); //captura en JwtAccessTokenConverter
	}

	@Bean //genera el JwtAccessTokenConverter con una clave
	public JwtAccessTokenConverter accessTokenConverter() { 
		JwtAccessTokenConverter tokenConverter=new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtk);
		return tokenConverter;
	}
	
}
