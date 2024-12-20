package com.cibertec.app.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.cibertec.app.model.Pedido;
import com.cibertec.app.model.Usuario;
import com.cibertec.app.service.PedidoService;
import com.cibertec.app.service.UsuarioService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirigir si no está autenticado
        }

        String email = principal.getName();
        Usuario usuario = usuarioService.findByEmail(email);

        if (usuario == null) {
            return "redirect:/login"; // Redirigir si el usuario no existe
        }

        List<Pedido> pedidos = pedidoService.getPedidosForUser(usuario);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("usuario", usuario);

        return "user/dashboard";
    }
}