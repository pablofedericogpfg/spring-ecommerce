package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuarios;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		// List<Producto> productos=productoService.findAll();
		LOGGER.info("Session de Usuario {}",session.getAttribute("idusuario"));
		model.addAttribute("productos", productoService.findAll());
		//Session
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		LOGGER.info("Id producto enviado como parametro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {

		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		Double sumaTotal = 0.00;

		Optional<Producto> optionalProducto = productoService.get(id);

		LOGGER.info("Producto añadido:{}", optionalProducto.get());
		LOGGER.info("cantidad:{}", cantidad);

		producto = optionalProducto.get();
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);
		detalleOrden.setImagen(producto.getImagen());
		// detalleOrden.setId(id);

		// validar que le producto no se añada 2 veces
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

		if (!ingresado) {
			detalles.add(detalleOrden);

		} else {
			for (DetalleOrden detalleOrdenx : detalles) {
				if (detalleOrdenx.getProducto().getId() == id) {
					detalleOrdenx.setCantidad(cantidad);
					detalleOrdenx.setTotal(producto.getPrecio() * cantidad);
				}

			}

		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles); // inyectar en cart los detalles
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	// Quitar un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		// Lista nueva de productos
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}

		}

		detalles = ordenesNueva;
		Double sumaTotal = 0.00;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles); // inyectar en cart los detalles
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		model.addAttribute("cart", detalles); // inyectar en cart los detalles
		model.addAttribute("orden", orden);
		
		//session
		model.addAttribute("sesion",session.getAttribute("idusuario"));
		

		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model,HttpSession session) {
		//session.getAttribute("idusuario")
		if ( session.getAttribute("idusuario")==null) {
			
			return "/usuario/login";
		}		
		if (detalles.isEmpty()) {
			return "redirect:/";
		}		
		Usuarios usuario=usuarioService.findById(Integer.parseInt( session.getAttribute("idusuario").toString())).get();
		model.addAttribute("cart", detalles); // inyectar en cart los detalles
		model.addAttribute("orden", orden);
		model.addAttribute("usuario",usuario);
		

		return "/usuario/resumenorden";
	}
	
	//Guardar la Orden
	@GetMapping("/saveOrden")
	public String saveOrden(HttpSession session) {
		
		if ( session.getAttribute("idusuario")==null) {
			
			return "/usuario/login";
		}
		if (detalles.isEmpty()) {
			return "redirect:/";
		}
		Date fechaCreacion= new Date();
		
		orden.setFechaRecibida(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//Usuario
		Usuarios usuario=usuarioService.findById(Integer.parseInt( session.getAttribute("idusuario").toString())).get();
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//guardar detalles
		for(DetalleOrden dt:detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		/// limpiar Orden y detalles
		orden=new Orden();
		detalles.clear();
		
		
		return "redirect:/";
	}
	
	//Busqueda
	@PostMapping("/search")
	public String searchPorduct(@RequestParam("nombre") String nombre,Model model) {
		LOGGER.info("Nombre del Producto: {}",nombre);
		
		List<Producto> productos=productoService.findAll().stream().filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase())).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		return "usuario/home";
	}
}
