package com.cibertec.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cibertec.app.dto.ProductoDTO;
import com.cibertec.app.dto.CategoriaDTO;
import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.CategoriaService;
import com.cibertec.app.service.CarritoService;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarProductos(Model model) {
        List<ProductoDTO> productos = productoService.listarTodos(); // Usar DTO para productos
        model.addAttribute("productos", productos);
        return "productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevoProducto(Model model) {
        model.addAttribute("producto", new ProductoDTO()); // Usar ProductoDTO
        List<CategoriaDTO> categorias = categoriaService.listarTodos(); // Usar DTO para categorías
        model.addAttribute("categorias", categorias);
        return "producto-form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarProducto(@PathVariable Long id, Model model) {
        ProductoDTO producto = productoService.obtenerPorId(id); // Usar DTO para productos
        if (producto != null) {
            model.addAttribute("producto", producto);
            List<CategoriaDTO> categorias = categoriaService.listarTodos(); // Usar DTO para categorías
            model.addAttribute("categorias", categorias);
            return "producto-form";
        } else {
            return "redirect:/admin/productos";
        }
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoDTO productoDTO, @RequestParam Long categoriaId) {
        CategoriaDTO categoriaDTO = categoriaService.obtenerPorId(categoriaId); // Usar DTO para categorías
        productoDTO.setCategoria(categoriaDTO);
        productoService.guardar(productoDTO); // Guardar usando DTO
        return "redirect:/admin/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/admin/productos";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<ProductoDTO> productos = productoService.listarTodos(); // Usar DTO para productos
        model.addAttribute("productos", productos);
        return "index";
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId, @RequestParam int cantidad, Principal principal) {
        String email = principal.getName();
        double cantidadDouble = (double) cantidad; // Conversión de int a double
        carritoService.agregarProducto(email, productoId, cantidadDouble); // Pasa el valor convertido
        return "redirect:/";
    }

}
