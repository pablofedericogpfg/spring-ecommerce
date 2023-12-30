package com.curso.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.curso.ecommerce.model.Usuarios;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuarios, Integer> {
	
	Optional <Usuarios> findByMail(String mail);
	

}
