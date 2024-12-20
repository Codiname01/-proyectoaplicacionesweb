package com.cibertec.app.controller;

import com.cibertec.app.dto.UsuarioDTO;
import com.cibertec.app.dto.ProductoDTO;
import com.cibertec.app.dto.VentaDTO;
import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.UsuarioService;
import com.cibertec.app.service.VentaService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final VentaService ventaService;

    public AdminController(UsuarioService usuarioService, ProductoService productoService, VentaService ventaService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.ventaService = ventaService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/admin_dashboard";
    }

    @GetMapping("/usuarios")
    public String gestionUsuarios(Model model) {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos(); // Ajustado para usar UsuarioDTO
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    @GetMapping("/productos")
    public String gestionProductos(Model model) {
        List<ProductoDTO> productos = productoService.listarTodos(); // Ajustado para usar ProductoDTO
        model.addAttribute("productos", productos);
        return "admin/productos";
    }

    @GetMapping("/reporte-usuarios")
    public String reporteUsuarios(Model model) {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos(); // Ajustado para usar UsuarioDTO
        model.addAttribute("usuarios", usuarios);
        return "admin/reporte-usuarios";
    }

    @GetMapping("/reporte-productos")
    public String reporteProductos(Model model) {
        List<ProductoDTO> productos = productoService.listarTodos(); // Ajustado para usar ProductoDTO
        model.addAttribute("productos", productos);
        return "admin/reporte-productos";
    }

    @GetMapping("/ventas")
    public String gestionVentas(Model model) {
        List<VentaDTO> ventas = ventaService.obtenerTodasLasVentas(); // Ajustado para usar VentaDTO
        model.addAttribute("ventas", ventas);
        return "admin/ventas";
    }
}
