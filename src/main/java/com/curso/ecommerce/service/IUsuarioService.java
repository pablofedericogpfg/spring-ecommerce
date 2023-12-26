package com.curso.ecommerce.service;

import java.util.Optional;

import com.curso.ecommerce.model.Usuarios;

public interface IUsuarioService {
Optional<Usuarios> findById(Integer id);
	
}
