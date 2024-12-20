package com.cibertec.app.dto;

public class VentaItemDTO {

    private Long id;
    private Long productoId; // Representa el ID del producto
    private String productoNombre; // Nombre del producto
    private Double precio;
    private Double cantidad; // Ajustado para ser consistente con el tipo Double

    // Constructor vacío
    public VentaItemDTO() {
    }

    // Constructor con parámetros
    public VentaItemDTO(Long id, Long productoId, String productoNombre, Double precio, Double cantidad) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Métodos Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

}
