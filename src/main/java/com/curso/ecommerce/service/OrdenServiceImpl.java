package com.curso.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

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
	@Override
	public List<Orden> findAll() {
		// TODO Auto-generated method stub
		return oredenRepository.findAll();
	}
	
	
	public String generarNumeroOrden() {
		int numero=0;
		
		
		
		List<Orden> ordenes= findAll();
		List<Integer> numeros= new ArrayList<Integer>();
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		
		if (ordenes.isEmpty()) {
			numero=1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();
			numero++;
		}
		String numeroConcatenado="";
		/*
		if (numero<10) {
			numeroConcatenado=""+String.valueOf(numero);
			
		} else if(numero<100)
		*/
		numeroConcatenado=String.format("%0" + 10 + "d", Integer.valueOf(numero));
		return numeroConcatenado;
	}

}
