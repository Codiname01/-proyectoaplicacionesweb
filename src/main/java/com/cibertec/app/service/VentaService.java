package com.cibertec.app.service;

import com.cibertec.app.dto.VentaDTO;
import com.cibertec.app.dto.VentaItemDTO;
import com.cibertec.app.model.*;
import com.cibertec.app.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CarritoService carritoService;

    @Transactional
    public Venta crearVentaDesdeCarrito(String email) {
        Carrito carrito = carritoService.getCarritoForUser(email);
        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        final Venta venta = new Venta(); // Declaramos 'venta' como final
        venta.setUsuario(carrito.getUsuario());
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTotal(carrito.getTotal());

        List<VentaItem> items = carrito.getItems().stream()
                .map(item -> {
                    VentaItem ventaItem = new VentaItem();
                    ventaItem.setVenta(venta);
                    ventaItem.setProducto(item.getProducto());
                    ventaItem.setCantidad(item.getCantidad());
                    ventaItem.setPrecio(item.getProducto().getPrecio());
                    return ventaItem;
                }).collect(Collectors.toList());

        venta.setItems(items);
        ventaRepository.save(venta);
        carritoService.vaciarCarrito(email);

        return venta;
    }


    // Convierte una VentaDTO a Venta (entidad)
    public Venta convertirAVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setId(ventaDTO.getId());
        venta.setFechaVenta(ventaDTO.getFechaVenta());
        venta.setTotal(ventaDTO.getTotal());

        Usuario usuario = new Usuario();
        usuario.setId(ventaDTO.getUsuarioId());
        venta.setUsuario(usuario);

        List<VentaItem> items = ventaDTO.getItems().stream()
                .map(itemDTO -> {
                    VentaItem ventaItem = new VentaItem();
                    Producto producto = new Producto();
                    producto.setId(itemDTO.getProductoId());

                    ventaItem.setProducto(producto);
                    ventaItem.setCantidad(itemDTO.getCantidad());
                    ventaItem.setPrecio(itemDTO.getPrecio());
                    ventaItem.setVenta(venta);
                    return ventaItem;
                }).collect(Collectors.toList());

        venta.setItems(items);
        return venta;
    }

    // Convierte una Venta (entidad) a VentaDTO
    private VentaDTO convertirAVentaDTO(Venta venta) {
        return new VentaDTO(
                venta.getId(),
                venta.getUsuario().getId(),
                venta.getUsuario().getNombre(),
                venta.getFechaVenta(),
                venta.getTotal(),
                venta.getItems().stream()
                        .map(item -> new VentaItemDTO(
                                item.getId(),
                                item.getProducto().getId(),
                                item.getProducto().getNombre(),
                                item.getPrecio(),
                                item.getCantidad()
                        )).collect(Collectors.toList())
        );
    }

    // Retorna todas las ventas como DTOs
    public List<VentaDTO> obtenerTodasLasVentas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirAVentaDTO)
                .collect(Collectors.toList());
    }

    // Retorna una venta por ID como DTO
    public VentaDTO obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .map(this::convertirAVentaDTO)
                .orElse(null);
    }

    // Retorna todas las ventas de un usuario específico
    public List<Venta> obtenerVentasPorUsuario(Usuario usuario) {
        return ventaRepository.findByUsuarioOrderByFechaVentaDesc(usuario);
    }

    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    // Elimina una venta por ID
    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }
}
