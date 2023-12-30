package com.curso.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuarios;
import com.curso.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
	
	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Optional<Usuarios> findById(Integer id) {
		// TODO Auto-generated method stub
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuarios save(Usuarios usuario) {
		// TODO Auto-generated method stub
		return usuarioRepository.saveAndFlush(usuario);
	}

	@Override
	public Optional<Usuarios> findByMail(String mail) {
		// TODO Auto-generated method stub
		
		return usuarioRepository.findByMail(mail);
	}


}
