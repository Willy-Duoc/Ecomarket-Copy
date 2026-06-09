package com.ecomarket.procesopagoservice.repository;

import com.ecomarket.procesopagoservice.model.TransaccionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<TransaccionPago, Long> {
    List<TransaccionPago> findByClienteId(Long clienteId);
    List<TransaccionPago> findByPedidoId(Long pedidoId);
}
