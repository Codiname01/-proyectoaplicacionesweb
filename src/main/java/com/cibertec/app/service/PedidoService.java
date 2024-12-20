package com.cibertec.app.service;

import com.cibertec.app.model.Pedido;
import com.cibertec.app.model.Usuario;
import com.cibertec.app.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Crea o actualiza un pedido en la base de datos.
     */
    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    /**
     * Devuelve la lista completa de pedidos (sin filtrar).
     */
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    /**
     * Guarda un pedido (equivalente a savePedido).
     */
    public void guardar(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    /**
     * Obtiene un pedido por su ID. Devuelve null si no existe.
     */
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    /**
     * Elimina un pedido por su ID.
     */
    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }

    /**
     * Devuelve la lista de pedidos asociada a un objeto Usuario específico.
     * Requiere un método findByUsuario(Usuario usuario) en PedidoRepository.
     */
    public List<Pedido> getPedidosForUser(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }

    /**
     * Devuelve la lista de pedidos filtrada por el email del usuario.
     * Requiere un método findByUsuarioEmail(String email) en PedidoRepository.
     */
    public List<Pedido> obtenerPedidosPorUsuario(String email) {
        return pedidoRepository.findByUsuarioEmail(email);
    }
}
