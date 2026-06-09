package com.ecomarket.procesopagoservice.repository;

import com.ecomarket.procesopagoservice.model.FacturaElectronica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<FacturaElectronica, Long> {
    Optional<FacturaElectronica> findByTransaccionId(Long transaccionId);
}
