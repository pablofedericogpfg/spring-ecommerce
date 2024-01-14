package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuarios;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	private final Logger LOGGER = LoggerFactory.getLogger(AdministradorController.class);
	@Autowired
	private IProductoService productoService;
	@Autowired
	private IUsuarioService usuarioService;	

	List<Usuarios> usuarios = new ArrayList<Usuarios>();

	@Autowired
	private IOrdenService ordenService;
	
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		LOGGER.info("Session de Usuario {}",session.getAttribute("idusuario"));
		List<Producto> productos=productoService.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	};

	@GetMapping("/usuarios")
	public String Usuarios(HttpSession session,Model model) {
		Usuarios usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()))
				.get();
		if (usuario.getTipo().equals("ADMIN")) {
			model.addAttribute("usuarios", usuarioService.findAll());
			return "administrador/usuarios";
		}
		return "redirect:/";
	}
	
	@GetMapping("/ordenes")
	public String ordenes(HttpSession session,Model model) {
		Usuarios usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()))
				.get();
		if (usuario.getTipo().equals("ADMIN")) {
			model.addAttribute("ordenes", ordenService.findAll());
			return "administrador/ordenes";
		}
		return "redirect:/";		
		
	}
	@GetMapping("/detalle/{id}")
	public String detalleOrden(@PathVariable Integer id, HttpSession session,Model model) {
		//session
		LOGGER.info("Id de la Orden: {}",id);
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		Orden orden = ordenService.findById(id).get();
		model.addAttribute("detalles",orden.getDetalle());
		
		return "administrador/detalleorden";
	}	
	@PostMapping("/search")
	public String searchPorduct(@RequestParam("nombre") String nombre, Model model) {
		LOGGER.info("Nombre del Producto: {}", nombre);

		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase())).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		return "administrador/home";	}

	
}
