package com.kalettre.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter, Ordered{

	private final Logger log= LoggerFactory.getLogger(EjemploGlobalFilter.class);
	
	@Override											//response y request
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		// se ejecuta esto antes 
		log.info("Ejecutando filtro pre");
		exchange.getRequest().mutate().headers(h-> h.add("tokenH", "123456"));
		
		//se ejecuta esto despues			//post
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			log.info("ejecutando filtro post");
			
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("tokenH")).ifPresent(valor->{
				exchange.getResponse().getHeaders().add("tokenH", valor);
			});
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color","rojo").build());
			//exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
