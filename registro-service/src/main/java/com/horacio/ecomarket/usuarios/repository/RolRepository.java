package com.horacio.ecomarket.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.horacio.ecomarket.usuarios.model.Rol;
import com.horacio.ecomarket.usuarios.model.RolNombre;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(RolNombre nombre);
}
