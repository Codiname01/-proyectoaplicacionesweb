package com.cibertec.app.controller;

import com.cibertec.app.model.Carrito;
import com.cibertec.app.service.CarritoService;
import com.cibertec.app.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class CheckoutController {
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private CarritoService carritoService;

    @GetMapping("/checkout")
    public String mostrarCheckout(Model model, Principal principal) {
        String email = principal.getName();
        Carrito carrito = carritoService.getCarritoForUser(email);
        model.addAttribute("carrito", carrito);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String procesarCheckout(Principal principal) {
        String email = principal.getName();
        ventaService.crearVentaDesdeCarrito(email);
        return "redirect:/confirmacion-compra";
    }

    @GetMapping("/confirmacion-compra")
    public String mostrarConfirmacionCompra() {
        return "confirmacion-compra";
    }
}