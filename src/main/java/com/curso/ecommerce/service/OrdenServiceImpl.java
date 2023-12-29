package com.curso.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService{
	
	@Autowired
	private IOrdenRepository oredenRepository;
	@Override
	public Orden save(Orden orden) {
		// TODO Auto-generated method stub
		return oredenRepository.save(orden);
	}
	

}
