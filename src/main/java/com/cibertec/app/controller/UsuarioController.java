package com.cibertec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.cibertec.app.dto.UsuarioDTO;
import com.cibertec.app.service.UsuarioService;

import java.util.List;

@Controller
@RequestMapping("/admin/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos(); // Cambiado para usar UsuarioDTO
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios"; // Vista: admin/usuarios
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevoUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioDTO()); // Cambiado a UsuarioDTO
        return "admin/usuario-form"; // Vista: admin/usuario-form
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerUsuarioDTOPorId(id); // Usar UsuarioDTO
            model.addAttribute("usuario", usuario);
            return "admin/usuario-form"; // Vista: admin/usuario-form
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado: " + e.getMessage());
            return "redirect:/admin/usuario";
        }
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute UsuarioDTO usuarioDTO, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.guardarDesdeDTO(usuarioDTO); // Guardar usando el DTO
            redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuario";
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(@ModelAttribute UsuarioDTO usuarioDTO, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizarDesdeDTO(usuarioDTO); // Actualizar usando el DTO
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuario";
    }
}
