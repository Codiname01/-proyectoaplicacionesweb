package com.cibertec.app.repository;

import com.cibertec.app.model.Venta;
import com.cibertec.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuarioOrderByFechaVentaDesc(Usuario usuario);
}