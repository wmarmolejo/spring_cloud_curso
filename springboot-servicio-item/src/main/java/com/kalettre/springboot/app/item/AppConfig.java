package com.kalettre.springboot.app.item;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class AppConfig {

	@Bean("clienteRest")
	@LoadBalanced
	public RestTemplate registarRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean //configuracion del resilience4j para el circuitbreaker desde clase
	//NOTA IMPORTANTE: SI SE CONFIGURA EL Resilience4J POR YML O PROPORTIES NO SE TOMARÁ ESTA CONFIGURACION 
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(){
		return factory->factory.configureDefault(id->{
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							.slidingWindowSize(10) //cantidad de llamados
							.failureRateThreshold(50) //porcentaje de fallo
							.waitDurationInOpenState(Duration.ofSeconds(10L)) //duracion del metodo alternativo
							.permittedNumberOfCallsInHalfOpenState(5) //peticiones en el estado semi abierto, para que vuelva a entrar al circuitbreaker
							.slowCallRateThreshold(50) //porcentaje de llamadas lentas
							.slowCallDurationThreshold(Duration.ofSeconds(2L))//configuracion de una llamada lenta
							.build())
					.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3L)).build()) //tiempo limete del timeout
					.build();
		});
	} 
	
	
}
