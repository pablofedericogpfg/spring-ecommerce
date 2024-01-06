package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuarios;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;
	

	@Autowired
	private IOrdenService ordenService;

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
		
		Usuarios usuario=usuarioService.findById(Integer.parseInt( session.getAttribute("idusuario").toString())).get();
		model.addAttribute("ordenes",ordenService.findByUsuario(usuario));
		
		
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		return "/usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detallecompra(@PathVariable Integer id, HttpSession session,Model model) {
		//session
		LOGGER.info("Id de la Orden: {}",id);
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		Orden orden = ordenService.findById(id).get();
		model.addAttribute("detalles",orden.getDetalle());
		
		return "usuario/detallecompra";
	}
	
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		
		session.removeAttribute("idusuario");
		return "redirect:/";
		
	}
	
	
}
