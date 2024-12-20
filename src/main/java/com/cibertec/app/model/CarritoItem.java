package com.cibertec.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @EmbeddedId
    private CarritoProductoId id;

    @ManyToOne
    @MapsId("carritoId")
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Double cantidad; // Cambiado a Double para evitar errores

    // Constructor
    public CarritoItem() {
        this.id = new CarritoProductoId();
    }

    // Getters y Setters
    public CarritoProductoId getId() {
        return id;
    }

    public void setId(CarritoProductoId id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
        if (this.id != null) {
            this.id.setCarritoId(carrito.getId());
        }
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (this.id != null) {
            this.id.setProductoId(producto.getId());
        }
    }

    public Double getCantidad() { // Retorna Double
        return cantidad;
    }

    public void setCantidad(Double cantidad) { // Ajustado para Double
        this.cantidad = cantidad;
    }
}
