package com.kalettre.springboot.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component	//Ejemplo<-es el nombre de la clase, este se especifica en el filtro application.yml
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion>{

	private final Logger log= LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);
	
	
	
	public static class Configuracion {
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
		
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
	}

	
	
	public EjemploGatewayFilterFactory() {
		super(Configuracion.class); 
	}



	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("mensaje","cookieNombre","cookieValor");
	}



	@Override
	public String name() {		
		return "NombreFilterCookie";
	}



	@Override
	public GatewayFilter apply(Configuracion config) {		
		return (exchange, chain)->{
			log.info("Ejecutando pre gateway filter factory, mensaje: "+config.mensaje);
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				Optional.ofNullable(config.cookieValor).ifPresent(cookie->{
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
				});
				log.info("Ejecutando post gateway filter factory, valor: "+config.cookieValor);
			})) ;
			
		};
	}

}
