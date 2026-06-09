package com.ecomarket.gestiontiendaservice.repository;

import com.ecomarket.gestiontiendaservice.model.PermisoPOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisoPOSRepository extends JpaRepository<PermisoPOS, Long> {
    Optional<PermisoPOS> findByRolEmpleado(Long rolEmpleado);
}