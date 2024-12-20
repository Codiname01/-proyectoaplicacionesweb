package com.cibertec.app.service;

import com.cibertec.app.model.*;
import com.cibertec.app.repository.CarritoRepository;
import com.cibertec.app.repository.ProductoRepository;
import com.cibertec.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Carrito getCarritoForUser(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    @Transactional
    public void agregarProducto(String email, Long productoId, Double cantidad) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });

        CarritoItem carritoItem = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseGet(() -> {
                    CarritoItem nuevoItem = new CarritoItem();
                    nuevoItem.setCarrito(carrito);
                    nuevoItem.setProducto(producto);
                    nuevoItem.setCantidad(0.0);
                    carrito.getItems().add(nuevoItem);
                    return nuevoItem;
                });

        carritoItem.setCantidad(carritoItem.getCantidad() + cantidad);

        actualizarTotalCarrito(carrito);
        carritoRepository.save(carrito);
    }

    private void actualizarTotalCarrito(Carrito carrito) {
        double total = carrito.getItems().stream()
                .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();
        carrito.setTotal(total);
    }

    @Transactional
    public void vaciarCarrito(String email) {
        Carrito carrito = getCarritoForUser(email);
        carrito.getItems().clear();
        carrito.setTotal(0.0);
        carritoRepository.save(carrito);
    }

    @Transactional
    public void realizarCompra(String email) {
        Carrito carrito = getCarritoForUser(email);
        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setFecha(LocalDateTime.now());
        pedido.setTotal(carrito.getTotal());

        for (CarritoItem item : carrito.getItems()) {
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setProducto(item.getProducto());
            pedidoItem.setCantidad(item.getCantidad().intValue()); // Conversión a Integer
            pedidoItem.setPrecio(item.getProducto().getPrecio());
            pedido.addItem(pedidoItem);
        }

        pedidoService.savePedido(pedido);
        vaciarCarrito(email);
    }


    @Transactional
    public void eliminarProducto(String email, Long productoId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoItem itemAEliminar = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        carrito.getItems().remove(itemAEliminar);
        actualizarTotalCarrito(carrito);
        carritoRepository.save(carrito);
    }

    @Transactional
    public void eliminarUnidad(String email, Long productoId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoItem itemAEliminar = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        if (itemAEliminar.getCantidad() > 1) {
            itemAEliminar.setCantidad(itemAEliminar.getCantidad() - 1.0);
        } else {
            carrito.getItems().remove(itemAEliminar);
        }

        actualizarTotalCarrito(carrito);
        carritoRepository.save(carrito);
    }

    @Transactional
    public void agregarUnidad(String email, Long productoId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoItem itemAAgregar = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (itemAAgregar != null) {
            itemAAgregar.setCantidad(itemAAgregar.getCantidad() + 1.0);
        } else {
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(1.0);
            carrito.getItems().add(nuevoItem);
        }

        actualizarTotalCarrito(carrito);
        carritoRepository.save(carrito);
    }
}
