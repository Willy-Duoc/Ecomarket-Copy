package com.horacio.ecomarket.usuarios.repository;

import com.horacio.ecomarket.usuarios.model.EstadoPerfil;
import com.horacio.ecomarket.usuarios.model.EstadoPerfilEnum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoPerfilRepository extends JpaRepository<EstadoPerfil, Long> {

    Optional<EstadoPerfil> findByNombre(EstadoPerfilEnum nombre);
}
