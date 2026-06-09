package com.horacio.ecomarket.usuarios.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.horacio.ecomarket.usuarios.dto.ConfigurarPermisosDTO;
import com.horacio.ecomarket.usuarios.dto.ModificarUsuarioDTO;
import com.horacio.ecomarket.usuarios.dto.RegistroUsuarioDTO;
import com.horacio.ecomarket.usuarios.model.EstadoPerfil;
import com.horacio.ecomarket.usuarios.model.PerfilUsuario;
import com.horacio.ecomarket.usuarios.model.Permiso;
import com.horacio.ecomarket.usuarios.model.Rol;
import com.horacio.ecomarket.usuarios.repository.EstadoPerfilRepository;
import com.horacio.ecomarket.usuarios.repository.PermisoRepository;
import com.horacio.ecomarket.usuarios.repository.RolRepository;
import com.horacio.ecomarket.usuarios.service.RegistroUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final RegistroUsuarioService service;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final EstadoPerfilRepository estadoPerfilRepository;

    // POST /api/usuarios/registro
    // rolId y estadoPerfilId son opcionales: si no vienen, el service asigna CLIENTE + ACTIVO
    @PostMapping("/registro")
    public ResponseEntity<PerfilUsuario> registrar(@Valid @RequestBody RegistroUsuarioDTO dto) {
        PerfilUsuario perfil = buildPerfilDesdeRegistroDTO(dto);
        PerfilUsuario creado = service.registrarCuenta(perfil, dto.getContrasenaInicial());
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/usuarios/{id}
    // Permite modificar nombre, correo, teléfono, rol y estadoPerfil
    // Si se cambia el rol, los permisos se actualizan automáticamente
    @PutMapping("/{id}")
    public ResponseEntity<PerfilUsuario> modificar(
            @PathVariable Long id,
            @Valid @RequestBody ModificarUsuarioDTO dto) {
        PerfilUsuario datosNuevos = buildPerfilDesdeModificarDTO(dto);
        return ResponseEntity.ok(service.modificarDatosUsuario(id, datosNuevos));
    }

    // GET /api/usuarios
    @GetMapping
    public ResponseEntity<List<PerfilUsuario>> listarTodos() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PerfilUsuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // GET /api/usuarios/correo/{correo}
    @GetMapping("/correo/{correo}")
    public ResponseEntity<PerfilUsuario> buscarPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(service.buscarPorCorreo(correo));
    }

    // GET /api/usuarios/rol/{rolId}
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<PerfilUsuario>> listarPorRol(@PathVariable Long rolId) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));
        return ResponseEntity.ok(service.listarPorRol(rol));
    }

    // PUT /api/usuarios/{id}/permisos
    // Permite sobreescribir manualmente los permisos, independiente del rol
    @PutMapping("/{id}/permisos")
    public ResponseEntity<Boolean> configurarPermisos(
            @PathVariable Long id,
            @Valid @RequestBody ConfigurarPermisosDTO dto) {
        List<Permiso> permisos = dto.getPermisoNombres().stream()
                .map(nombre -> permisoRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + nombre)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(service.configurarPermisos(id, permisos));
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Long id) {
        return ResponseEntity.ok(service.eliminarUsuario(id));
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private PerfilUsuario buildPerfilDesdeRegistroDTO(RegistroUsuarioDTO dto) {
        PerfilUsuario.PerfilUsuarioBuilder builder = PerfilUsuario.builder()
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .telefono(dto.getTelefono());

        // Solo asignar rol si viene explícitamente; si no, el service aplica CLIENTE por defecto
        if (dto.getRolId() != null) {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRolId()));
            builder.rol(rol);
        }

        // Solo asignar estado si viene explícitamente; si no, el service aplica ACTIVO por defecto
        if (dto.getEstadoPerfilId() != null) {
            EstadoPerfil estado = estadoPerfilRepository.findByNombre(dto.getEstadoPerfilId())
                    .orElseThrow(() -> new RuntimeException(
                            "EstadoPerfil no encontrado: " + dto.getEstadoPerfilId()));
            builder.estadoPerfil(estado);
        }

        return builder.build();
    }

    private PerfilUsuario buildPerfilDesdeModificarDTO(ModificarUsuarioDTO dto) {
        PerfilUsuario.PerfilUsuarioBuilder builder = PerfilUsuario.builder()
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .telefono(dto.getTelefono());

        if (dto.getRolId() != null) {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRolId()));
            builder.rol(rol);
        }

        if (dto.getEstadoPerfilId() != null) {
            EstadoPerfil estado = estadoPerfilRepository.findById(dto.getEstadoPerfilId())
                    .orElseThrow(() -> new RuntimeException(
                            "EstadoPerfil no encontrado con ID: " + dto.getEstadoPerfilId()));
            builder.estadoPerfil(estado);
        }

        return builder.build();
    }
}
