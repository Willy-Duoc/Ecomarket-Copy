package com.horacio.ecomarket.usuarios.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.horacio.ecomarket.usuarios.client.IniciosesionClient;
import com.horacio.ecomarket.usuarios.model.EstadoPerfil;
import com.horacio.ecomarket.usuarios.model.EstadoPerfilEnum;
import com.horacio.ecomarket.usuarios.model.PerfilUsuario;
import com.horacio.ecomarket.usuarios.model.Permiso;
import com.horacio.ecomarket.usuarios.model.Rol;
import com.horacio.ecomarket.usuarios.model.RolNombre;
import com.horacio.ecomarket.usuarios.repository.EstadoPerfilRepository;
import com.horacio.ecomarket.usuarios.repository.PerfilUsuarioRepository;
import com.horacio.ecomarket.usuarios.repository.PermisoRepository;
import com.horacio.ecomarket.usuarios.repository.RolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistroUsuarioServiceImpl implements RegistroUsuarioService {

    private final PerfilUsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final IniciosesionClient iniciosesionClient;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final EstadoPerfilRepository estadoPerfilRepository;

    @Override
    @Transactional
    public PerfilUsuario registrarCuenta(PerfilUsuario perfilUsuario, String contrasenaInicial) {
        repository.findByCorreo(perfilUsuario.getCorreo())
                .ifPresent(u -> {
                    throw new RuntimeException("El correo ya está registrado: " + perfilUsuario.getCorreo());
                });

        // Si no viene un rol desde el DTO, asignar CLIENTE por defecto
        if (perfilUsuario.getRol() == null) {
            Rol rolCliente = rolRepository.findByNombre(RolNombre.CLIENTE)
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado. ¿Se ejecutó el DataSeeder?"));
            perfilUsuario.setRol(rolCliente);
        }

        // Si no viene un estado, asignar ACTIVO por defecto
        if (perfilUsuario.getEstadoPerfil() == null) {
            EstadoPerfil estadoActivo = estadoPerfilRepository.findByNombre(EstadoPerfilEnum.ACTIVO)
                    .orElseThrow(() -> new RuntimeException("EstadoPerfil ACTIVO no encontrado. ¿Se ejecutó el DataSeeder?"));
            perfilUsuario.setEstadoPerfil(estadoActivo);
        }

        // Asignar los permisos correspondientes al rol asignado
        List<Permiso> permisosDelRol = obtenerPermisosPorRol(perfilUsuario.getRol().getNombre());
        perfilUsuario.getPermisos().clear();
        perfilUsuario.getPermisos().addAll(permisosDelRol);

        String contrasenaHasheada = passwordEncoder.encode(contrasenaInicial);
        perfilUsuario.setPassword(contrasenaHasheada);
        perfilUsuario.setFechaCreacion(LocalDateTime.now());

        PerfilUsuario creado = repository.save(perfilUsuario);

        iniciosesionClient.crearCredencial(
                creado.getId(),
                creado.getCorreo(),
                contrasenaHasheada
        );

        return creado;
    }

    @Override
    @Transactional
    public PerfilUsuario modificarDatosUsuario(Long id, PerfilUsuario datosNuevos) {
        PerfilUsuario existente = buscarPorId(id);

        existente.setNombre(datosNuevos.getNombre());
        existente.setTelefono(datosNuevos.getTelefono());

        if (!existente.getCorreo().equals(datosNuevos.getCorreo())) {
            repository.findByCorreo(datosNuevos.getCorreo())
                    .ifPresent(u -> {
                        throw new RuntimeException("El correo ya está en uso: " + datosNuevos.getCorreo());
                    });
            existente.setCorreo(datosNuevos.getCorreo());
        }

        // Si se cambia el rol, actualizar permisos automáticamente
        if (datosNuevos.getRol() != null &&
            !datosNuevos.getRol().getNombre().equals(existente.getRol().getNombre())) {
            existente.setRol(datosNuevos.getRol());
            List<Permiso> nuevosPermisos = obtenerPermisosPorRol(datosNuevos.getRol().getNombre());
            existente.getPermisos().clear();
            existente.getPermisos().addAll(nuevosPermisos);
        }

        if (datosNuevos.getEstadoPerfil() != null) {
            existente.setEstadoPerfil(datosNuevos.getEstadoPerfil());
        }

        return repository.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilUsuario> listarUsuarios() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilUsuario> listarPorRol(Rol rolUsuario) {
        return repository.findByRol(rolUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilUsuario buscarPorId(Long usuarioId) {
        return repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilUsuario buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }

    @Override
    @Transactional
    public Boolean configurarPermisos(Long usuarioId, List<Permiso> nuevosPermisos) {
        PerfilUsuario usuario = buscarPorId(usuarioId);
        usuario.getPermisos().clear();
        usuario.getPermisos().addAll(nuevosPermisos);
        repository.save(usuario);
        return true;
    }

    @Override
    @Transactional
    public Boolean eliminarUsuario(Long usuarioId) {
        PerfilUsuario usuario = buscarPorId(usuarioId);
        repository.delete(usuario);
        return true;
    }

    /**
 * Define qué permisos corresponden a cada rol.
 * Al cambiar el rol de un usuario, se actualizan sus permisos automáticamente.
 */
    private List<Permiso> obtenerPermisosPorRol(RolNombre rolNombre) {
        List<String> nombresPermiso = switch (rolNombre) {
            case CLIENTE -> List.of("VER_CATALOGO", "REALIZAR_COMPRA", "VER_HISTORIAL");
            case VENDEDOR -> List.of("VER_CATALOGO", "GESTIONAR_PRODUCTOS", "VER_HISTORIAL", "VER_REPORTES");
            case ADMIN -> List.of("VER_CATALOGO", "REALIZAR_COMPRA", "VER_HISTORIAL",
                                    "GESTIONAR_USUARIOS", "GESTIONAR_PRODUCTOS",
                                    "VER_REPORTES", "GESTIONAR_TIENDA", "GESTIONAR_PERMISOS");
            case USUARIO -> List.of("VER_CATALOGO");
        };

        return nombresPermiso.stream()
                .map(nombre -> permisoRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + nombre)))
                .collect(java.util.stream.Collectors.toList());
    }
}