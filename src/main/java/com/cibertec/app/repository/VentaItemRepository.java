package com.cibertec.app.repository;

import com.cibertec.app.model.VentaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaItemRepository extends JpaRepository<VentaItem, Long> {
}