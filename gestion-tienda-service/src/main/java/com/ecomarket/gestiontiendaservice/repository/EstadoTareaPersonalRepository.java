package com.ecomarket.gestiontiendaservice.repository;

import com.ecomarket.gestiontiendaservice.model.EstadoTareaPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoTareaPersonalRepository extends JpaRepository<EstadoTareaPersonal, Long> {
    Optional<EstadoTareaPersonal> findByNombre(String nombre);
}