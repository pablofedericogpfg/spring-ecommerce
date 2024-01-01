package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Usuarios;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;
	
	//usuario/registro
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save( Usuarios usuario) {
		
		LOGGER.info("Usuario a registrar {} ",usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		
		
		return "/usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuarios usuario,HttpSession session) {
		LOGGER.info("Acceso: {}",usuario);
		Optional<Usuarios> user=usuarioService.findByMail(usuario.getMail());
		LOGGER.info("Usuario obtenido: {}",user);
		if(user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				LOGGER.info("Usuario Administrador: {}",user);
				return "redirect:/administrador";
			}
		} else {
			LOGGER.info("Usuario no existe");
		}
		return "redirect:/";
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model,HttpSession session) {
		
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		return "/usuario/compras";
	}
}
