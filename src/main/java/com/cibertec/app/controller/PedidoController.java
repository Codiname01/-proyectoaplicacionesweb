package com.cibertec.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cibertec.app.model.Pedido;
import com.cibertec.app.service.PedidoService;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "pedidos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevoPedido(Model model) {
        model.addAttribute("pedido", new Pedido());
        return "pedido-form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarPedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.obtenerPorId(id));
        return "pedido-form";
    }

    @PostMapping("/guardar")
    public String guardarPedido(@ModelAttribute Pedido pedido) {
        pedidoService.guardar(pedido);
        return "redirect:/pedido";
    }

    @PostMapping("/actualizar")
    public String actualizarPedido(@ModelAttribute Pedido pedido) {
        pedidoService.guardar(pedido);
        return "redirect:/pedido";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return "redirect:/pedido";
    }
    
    @Controller
    @RequestMapping("/mi-cuenta")
    public class MiCuentaController {
        
        @Autowired
        private PedidoService pedidoService; // Un servicio para manejar los pedidos

        @GetMapping
        public String mostrarMiCuenta(Model model, Principal principal) {
            String emailUsuario = principal.getName(); // Obtener el email del usuario autenticado
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(emailUsuario); // Obtener los pedidos
            model.addAttribute("pedidos", pedidos); // Pasar la lista de pedidos al modelo
            return "mi_cuenta"; // Nombre del archivo HTML
        }
    }

}
