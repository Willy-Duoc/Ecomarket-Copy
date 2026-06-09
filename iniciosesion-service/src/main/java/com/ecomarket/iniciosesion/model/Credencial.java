package com.ecomarket.iniciosesion.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "credenciales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long usuarioId;

    @Column(nullable = false, unique = true, length = 150)
    private String correoAcceso;

    @Column(nullable = false)
    private String contrasenaHash;

    @Column(nullable = false)
    @Builder.Default
    private Boolean cuentaBloqueada = false;

    private LocalDateTime fechaUltimoLogin;
}
