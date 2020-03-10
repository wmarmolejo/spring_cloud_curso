package com.kalettre.springboot.app.usuarios.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kalettre.springboot.app.usuarios.models.entity.Usuario;

//spring-boot-starter-data-rest para hacer uso de @RepositoryRestResource video60
@RepositoryRestResource(path="usuarios")
public interface UsuarioDao extends PagingAndSortingRepository<Usuario,Long>{

	public Usuario findByUsername(String username);
	
	@Query("select u from Usuario u where u.username=?1")
	public Usuario obtenerByUsername(String username);
	
}
