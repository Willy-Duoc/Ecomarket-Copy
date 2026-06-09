package com.ecomarket.gestiontiendaservice.repository;

import com.ecomarket.gestiontiendaservice.model.TareaPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaPersonalRepository extends JpaRepository<TareaPersonal, Long> {
    List<TareaPersonal> findByEmpleadoId(Long empleadoId);
    List<TareaPersonal> findBySucursalId(Long sucursalId);
}