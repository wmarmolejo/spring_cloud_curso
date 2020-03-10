package com.kalettre.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {
	private static Logger log= LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);
	
	//Validar para ejecutar o no el filtro	//Si retorna true ejecuta el metodo run, de lo contrario no
	@Override
	public boolean shouldFilter() {
		return true;
	}
	//Se resuelve la logica del filtro
	@Override
	public Object run() throws ZuulException {		
		RequestContext ctx= RequestContext.getCurrentContext();
		HttpServletRequest request=ctx.getRequest();
		log.info("Entrando a post");		
		Long tiempoInicio=(Long)request.getAttribute("tiempoInicio");
		Long tiempoFinal=System.currentTimeMillis();
		Long tiempoTranscurrido=tiempoFinal-tiempoInicio;
		log.info(String.format("Tiempo transcurrido en %s seg", tiempoTranscurrido.doubleValue()/1000.00));
		log.info(String.format("Tiempo transcurrido en %s mileseg", tiempoTranscurrido));
		return null;
	}

	//nombre filtro
	@Override
	public String filterType() {		
		return "post";
	}	
	//
	@Override
	public int filterOrder() {
		return 1;
	}

	
	
}
