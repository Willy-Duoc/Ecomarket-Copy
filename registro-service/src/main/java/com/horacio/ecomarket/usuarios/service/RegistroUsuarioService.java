package com.horacio.ecomarket.usuarios.service;

import java.util.List;

import com.horacio.ecomarket.usuarios.model.PerfilUsuario;
import com.horacio.ecomarket.usuarios.model.Permiso;
import com.horacio.ecomarket.usuarios.model.Rol;

public interface RegistroUsuarioService {

    // Registra un nuevo usuario con contraseña inicial
    PerfilUsuario registrarCuenta(PerfilUsuario perfilUsuario, String contrasenaInicial);

    // Modifica los datos de un usuario existente
    PerfilUsuario modificarDatosUsuario(Long id, PerfilUsuario datosNuevos);

    // Retorna lista de todos los usuarios
    List<PerfilUsuario> listarUsuarios();

    // Retorna lista de usuarios filtrados por rol
    List<PerfilUsuario> listarPorRol(Rol rolUsuario);

    // Busca un usuario por su ID
    PerfilUsuario buscarPorId(Long usuarioId);

    // Busca un usuario por su correo electrónico
    PerfilUsuario buscarPorCorreo(String correo);

    // Configura/reemplaza los permisos de un usuario
    Boolean configurarPermisos(Long usuarioId, List<Permiso> nuevosPermisos);

    // Elimina un usuario por su ID
    Boolean eliminarUsuario(Long usuarioId);
}
