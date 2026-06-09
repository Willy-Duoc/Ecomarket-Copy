package com.horacio.ecomarket.usuarios.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.horacio.ecomarket.usuarios.model.EstadoPerfil;
import com.horacio.ecomarket.usuarios.model.EstadoPerfilEnum;
import com.horacio.ecomarket.usuarios.model.Permiso;
import com.horacio.ecomarket.usuarios.model.Rol;
import com.horacio.ecomarket.usuarios.model.RolNombre;
import com.horacio.ecomarket.usuarios.repository.EstadoPerfilRepository;
import com.horacio.ecomarket.usuarios.repository.PermisoRepository;
import com.horacio.ecomarket.usuarios.repository.RolRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DataSeeder implements CommandLineRunner{
    
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final EstadoPerfilRepository estadoPerfilRepository;

    @Override
    public void run(String... args) {
        sembrarEstadosPerfil();
        sembrarPermisos();
        sembrarRoles();
        log.info(">>> DataSeeder completado.");
    }

    // ── 1. Estados de perfil ──────────────────────────────────────────────────

    private void sembrarEstadosPerfil() {
        for (EstadoPerfilEnum estado : EstadoPerfilEnum.values()) {
            if (estadoPerfilRepository.findByNombre(estado).isEmpty()) {
                estadoPerfilRepository.save(
                    EstadoPerfil.builder().nombre(estado).build()
                );
                log.info("EstadoPerfil sembrado: {}", estado);
            }
        }
    }

    // ── 2. Permisos base ──────────────────────────────────────────────────────

    private void sembrarPermisos() {
        List<String[]> permisosBase = List.of(
            new String[]{"VER_CATALOGO",        "Ver productos del catálogo"},
            new String[]{"REALIZAR_COMPRA",     "Agregar al carrito y realizar compras"},
            new String[]{"VER_HISTORIAL",       "Ver historial de pedidos propios"},
            new String[]{"GESTIONAR_USUARIOS",  "Crear, modificar y eliminar usuarios"},
            new String[]{"GESTIONAR_PRODUCTOS", "Crear, editar y eliminar productos"},
            new String[]{"VER_REPORTES",        "Acceder a reportes y analítica"},
            new String[]{"GESTIONAR_TIENDA",    "Administrar sucursales y personal"},
            new String[]{"GESTIONAR_PERMISOS",  "Asignar y revocar permisos a usuarios"}
        );

        for (String[] p : permisosBase) {
            if (permisoRepository.findByNombre(p[0]).isEmpty()) {
                permisoRepository.save(
                    Permiso.builder().nombre(p[0]).descripcion(p[1]).build()
                );
                log.info("Permiso sembrado: {}", p[0]);
            }
        }
    }

    // ── 3. Roles con sus descripciones ────────────────────────────────────────

    private void sembrarRoles() {
        Map<RolNombre, String> roles = Map.of(
            RolNombre.CLIENTE,  "Usuario cliente con acceso a compras",
            RolNombre.VENDEDOR, "Empleado con acceso a gestión de productos",
            RolNombre.ADMIN,    "Administrador con acceso total al sistema",
            RolNombre.USUARIO,  "Usuario base sin permisos adicionales"
        );

        for (Map.Entry<RolNombre, String> entry : roles.entrySet()) {
            if (rolRepository.findByNombre(entry.getKey()).isEmpty()) {
                rolRepository.save(
                    Rol.builder()
                        .nombre(entry.getKey())
                        .descripcion(entry.getValue())
                        .build()
                );
                log.info("Rol sembrado: {}", entry.getKey());
            }
        }
    }
}
