package com.cibertec.app.controller;

import com.cibertec.app.dto.CategoriaDTO;
import com.cibertec.app.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodos()); // Usando DTOs
        return "categorias";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevaCategoria(Model model) {
        model.addAttribute("categoria", new CategoriaDTO()); // Usar CategoriaDTO
        return "categoria-form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarCategoria(@PathVariable Long id, Model model) {
        CategoriaDTO categoria = categoriaService.obtenerPorId(id); // Usar DTO
        if (categoria != null) {
            model.addAttribute("categoria", categoria);
            return "categoria-form";
        } else {
            return "redirect:/categoria"; // Redirigir si no se encuentra la categoría
        }
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute CategoriaDTO categoriaDTO) {
        categoriaService.guardar(categoriaDTO); // Guardar usando DTO
        return "redirect:/categoria";
    }

    @PostMapping("/actualizar")
    public String actualizarCategoria(@ModelAttribute CategoriaDTO categoriaDTO) {
        categoriaService.guardar(categoriaDTO); // Actualizar usando DTO
        return "redirect:/categoria";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return "redirect:/categoria";
    }
}
