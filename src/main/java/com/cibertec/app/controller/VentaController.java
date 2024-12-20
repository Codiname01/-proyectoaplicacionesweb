package com.cibertec.app.controller;

import com.cibertec.app.model.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.cibertec.app.dto.UsuarioDTO;
import com.cibertec.app.dto.ProductoDTO;
import com.cibertec.app.dto.VentaDTO;
import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.UsuarioService;
import com.cibertec.app.service.VentaService;

import java.util.List;

@Controller
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar todas las ventas
    @GetMapping
    public String listarVentas(Model model) {
        List<VentaDTO> ventas = ventaService.obtenerTodasLasVentas();
        model.addAttribute("ventas", ventas);
        return "ventas"; // Asegúrate de tener un archivo ventas.html en tu carpeta de plantillas
    }

    // Mostrar formulario de nueva venta con listado de usuarios y productos
    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevaVenta(Model model) {
        model.addAttribute("venta", new VentaDTO());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("productos", productoService.listarTodos());
        return "venta-form";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute("venta") VentaDTO ventaDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "venta-form";
        }

        double total = ventaDTO.getItems().stream()
                .mapToDouble(item -> item.getPrecio() * item.getCantidad())
                .sum();
        ventaDTO.setTotal(total);

        // Convierte el DTO a la entidad Venta
        Venta venta = ventaService.convertirAVenta(ventaDTO);

        // Guarda la entidad
        ventaService.guardar(venta);

        return "redirect:/venta";
    }



    // Eliminar una venta
    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id) {
        ventaService.eliminar(id);
        return "redirect:/venta"; // Redirige a la lista de ventas después de eliminar
    }

    // Mostrar formulario de edición de venta con listado de usuarios y productos
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarVenta(@PathVariable Long id, Model model) {
        VentaDTO ventaDTO = ventaService.obtenerVentaPorId(id);
        if (ventaDTO != null) {
            // Listar usuarios y productos para los combos
            List<UsuarioDTO> usuarios = usuarioService.listarTodos();
            List<ProductoDTO> productos = productoService.listarTodos();

            // Pasar la venta y los datos al modelo
            model.addAttribute("venta", ventaDTO);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("productos", productos);

            return "venta-form";
        } else {
            return "redirect:/venta";
        }
    }
}
