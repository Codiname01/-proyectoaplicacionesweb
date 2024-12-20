package com.cibertec.app.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cibertec.app.model.Carrito;
import com.cibertec.app.service.CarritoService;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public String verCarrito(Model model, Principal principal) {
        String email = principal.getName();
        Carrito carrito = carritoService.getCarritoForUser(email);
        model.addAttribute("carrito", carrito);
        return "carrito";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId, @RequestParam int cantidad, Principal principal) {
        String email = principal.getName();
        carritoService.agregarProducto(email, productoId, (double) cantidad); // Conversión explícita a Double
        return "redirect:/carrito";
    }


    @PostMapping("/comprar")
    public String comprar(Principal principal) {
        String email = principal.getName();
        carritoService.realizarCompra(email);
        return "redirect:/user/dashboard";
    }
   
    @PostMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        carritoService.eliminarProducto(email, id);
        return "redirect:/carrito"; // Redirige al carrito después de eliminar el producto
    }
    @PostMapping("/eliminar-unidad/{id}")
    public String eliminarUnidadDelCarrito(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        carritoService.eliminarUnidad(email, id);
        return "redirect:/carrito"; // Redirige al carrito después de eliminar una unidad
    }
    @PostMapping("/agregar-unidad/{id}")
    public String agregarUnidadAlCarrito(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        carritoService.agregarUnidad(email, id); // Llama al servicio para agregar una unidad
        return "redirect:/carrito"; // Redirige al carrito después de agregar una unidad
    }


}