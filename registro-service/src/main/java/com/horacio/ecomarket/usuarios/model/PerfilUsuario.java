package com.horacio.ecomarket.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "perfil_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String password;

    private String telefono;

    private LocalDateTime fechaCreacion;

    // Relación con Rol
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    // Relación con EstadoPerfil
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_perfil_id")
    private EstadoPerfil estadoPerfil;

    // Relación con Permiso (muchos a muchos)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "perfil_permiso",
        joinColumns = @JoinColumn(name = "perfil_usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    @Builder.Default
    private List<Permiso> permisos = new ArrayList<>();
}
