package com.kalettre.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kalettre.springboot.app.item.models.Item;
import com.kalettre.springboot.app.commons.models.entity.Producto;
import com.kalettre.springboot.app.item.models.service.ItemService; 

@RefreshScope //sirve para mantener actualizado los componentes y el Enviroment
@RestController
public class ItemController {
	
	private final Logger log = LoggerFactory.getLogger(ItemController.class);
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Autowired
	private Environment env;
	@Autowired
	@Qualifier("itemFeign")//Para indicar que ItemService se debe inyectar
	//@Qualifier("itemImpl")
	private ItemService itemService;
	@Value("${configuracion.texto}")
	private String texto;
	
	@GetMapping("/listar")
	public List<Item> listar(@RequestParam(name="nombre", required=false) String nombreFilterHeader,
			@RequestHeader(name="token-request",required=false) String tokenRequestFilterHeader){
		//envio del request 
		System.out.println("------------------- nombreFilterHeader : "+ nombreFilterHeader);
		System.out.println("------------------- tokenRequestFilterHeader : "+ tokenRequestFilterHeader);
		return itemService.findAll();
	}
	
	//@HystrixCommand(fallbackMethod="metodoAlternativo") con hystrix, pero ya esta deprecado
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return cbFactory.create("items")
				.run(() -> itemService.findById(id, cantidad),	// intente ejecutar este
						e -> metodoAlternativo(id, cantidad, e)); //metodo a ejecutar en caso que falle
	}
	
	//Si la aplicación no se encuentra desplegada se ejecuta este metodo
	public Item metodoAlternativo( Long id,  Integer cantidad, Throwable e) {
		log.info("Throwable e: "+e.getMessage());
		Item item=new Item();
		item.setCantidad(cantidad);
		Producto producto=new Producto();
		producto.setId(id);
		producto.setNombre("Camara Sony");
		producto.setPrecio(500.00);
		item.setProducto(producto);
		return item;
	} 
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
		Map<String,String> json=new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		if(env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.email"));
		}
		return new ResponseEntity<Map<String,String>>(json,HttpStatus.OK);
	}
	
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return itemService.save(producto);		
	}
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto,@PathVariable Long id) {		
		return itemService.update(producto, id);		
	}

	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		itemService.delete(id);
	}
	
}

