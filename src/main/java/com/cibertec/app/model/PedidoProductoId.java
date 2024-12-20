package com.cibertec.app.model;

import java.io.Serializable;
import java.util.Objects;

public class PedidoProductoId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long pedidoId;
    private Long productoId;

    public PedidoProductoId() {}

    public PedidoProductoId(Long pedidoId, Long productoId) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
    }

    // Getters y setters

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoProductoId that = (PedidoProductoId) o;
        return Objects.equals(pedidoId, that.pedidoId) &&
               Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId, productoId);
    }
}


