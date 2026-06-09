package com.ecomarket.procesopagoservice.repository;

import com.ecomarket.procesopagoservice.model.MetodoPagoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPagoTransaccion, Long> {
    Optional<MetodoPagoTransaccion> findByNombre(String nombre);
}
