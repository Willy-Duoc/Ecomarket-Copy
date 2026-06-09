package com.ecomarket.gestiontiendaservice.repository;

import com.ecomarket.gestiontiendaservice.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    List<Sucursal> findByActivaTrue();
}