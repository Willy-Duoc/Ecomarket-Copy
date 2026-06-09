package com.ecomarket.procesopagoservice.repository;

import com.ecomarket.procesopagoservice.model.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadoPagoRepository extends JpaRepository<EstadoPago, Long> {
    Optional<EstadoPago> findByNombre(String nombre);
}
