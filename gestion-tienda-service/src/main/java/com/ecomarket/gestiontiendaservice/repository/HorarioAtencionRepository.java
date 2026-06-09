package com.ecomarket.gestiontiendaservice.repository;

import com.ecomarket.gestiontiendaservice.model.HorarioAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {
    List<HorarioAtencion> findBySucursalId(Long sucursalId);
}