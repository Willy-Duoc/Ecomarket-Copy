package com.ecomarket.procesopagoservice.repository;

import com.ecomarket.procesopagoservice.model.CuponDescuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository<CuponDescuento, Long> {
    Optional<CuponDescuento> findByCodigo(String codigo);
}
