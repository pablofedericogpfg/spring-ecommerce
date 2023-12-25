package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

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
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private ProductoService productoService;
	
	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles= new ArrayList<DetalleOrden>();
	
	// datos de la orden
	Orden orden = new Orden();
	
	@GetMapping("")
	public String home(Model model) {
		//List<Producto> productos=productoService.findAll();
		model.addAttribute("productos", productoService.findAll());		
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id,Model model) {
		LOGGER.info("Id producto enviado como parametro {}",id);
		Producto producto= new Producto();
		Optional<Producto> productoOptional= productoService.get(id);
		producto=productoOptional.get();
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id,@RequestParam Integer cantidad,Model model) {
		
		DetalleOrden detalleOrden=new DetalleOrden();
		Producto producto=new Producto();
		Double sumaTotal=0.00;
		
		Optional<Producto> optionalProducto=productoService.get(id);
		
		LOGGER.info("Producto añadido:{}",optionalProducto.get());
		LOGGER.info("cantidad:{}",cantidad);
		
		producto=optionalProducto.get();
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);
		detalleOrden.setImagen(producto.getImagen());
		
		detalles.add(detalleOrden);
		
		detalleOrden.setId(id);
		sumaTotal=detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart",detalles); // inyectar en cart los detalles
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}

}