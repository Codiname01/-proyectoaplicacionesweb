package com.cibertec.app.dto;

import java.time.LocalDateTime;
import java.util.List;

public class VentaDTO {

    private Long id;
    private Long usuarioId; // Solo el ID del usuario para simplificar la relación
    private String usuarioNombre; // Campo adicional para el nombre del usuario
    private LocalDateTime fechaVenta;
    private Double total;
    private List<VentaItemDTO> items; // Lista de ítems de la venta en forma de DTO

    // Constructores
    public VentaDTO() {
    }

    public VentaDTO(Long id, Long usuarioId, String usuarioNombre, LocalDateTime fechaVenta, Double total, List<VentaItemDTO> items) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.items = items;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<VentaItemDTO> getItems() {
        return items;
    }

    public void setItems(List<VentaItemDTO> items) {
        this.items = items;
    }
}
