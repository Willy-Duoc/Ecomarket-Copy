package com.ecomarket.gestiontiendaservice.repository;

import com.ecomarket.gestiontiendaservice.model.ReglamentoInterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReglamentoInternoRepository extends JpaRepository<ReglamentoInterno, Long> {
    Optional<ReglamentoInterno> findTopBySucursalIdOrderByVersionDesc(Long sucursalId);
}