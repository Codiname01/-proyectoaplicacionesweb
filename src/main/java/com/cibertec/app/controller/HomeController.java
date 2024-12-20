package com.cibertec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.CarritoService;
import com.cibertec.app.model.Producto;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoService carritoService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<Producto> productos = productoService.getAllProducts();
        model.addAttribute("productos", productos);
        return "index";
    }

	public CarritoService getCarritoService() {
		return carritoService;
	}

	public void setCarritoService(CarritoService carritoService) {
		this.carritoService = carritoService;
	}

   
}