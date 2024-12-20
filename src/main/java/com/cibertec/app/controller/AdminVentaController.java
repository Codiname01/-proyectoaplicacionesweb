package com.cibertec.app.controller;



import com.cibertec.app.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/listaventas")
public class AdminVentaController {
    @Autowired
    private VentaService ventaService;

    @GetMapping
    public String listarVentas(Model model) {
        model.addAttribute("ventas", ventaService.obtenerTodasLasVentas());
        return "admin/ventas/lista";
    }

    @GetMapping("/{id}")
    public String verDetallesVenta(@PathVariable Long id, Model model) {
        model.addAttribute("venta", ventaService.obtenerVentaPorId(id));
        return "admin/ventas/detalles";
    }

    // Generar un reporte de ventas
    @GetMapping("/reporte") // Cambia aquí para evitar conflictos
    public String reporteVentas(Model model) {
        model.addAttribute("ventas", ventaService.obtenerTodasLasVentas());
        return "admin/ventas/reporte";
    }
    
  
}