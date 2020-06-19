package com.kalettre.springboot.app.oauth.services;


import com.kalettre.springboot.app.commons.usuarios.models.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);

	public Usuario update(Usuario usuario, Long id);
	
}
