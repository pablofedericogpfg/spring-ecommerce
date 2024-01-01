package com.curso.ecommerce.service;



import java.util.List;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuarios;


public interface IOrdenService {
	List<Orden> findAll();
	Orden save(Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuarios usuario);

}
