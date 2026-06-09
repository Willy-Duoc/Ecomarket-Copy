package com.ecomarket.gestiontiendaservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarea_personal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long empleadoId;

    @Column
    private Long gerenteAsignadoId;

    @Column
    private Long sucursalId;

    @Column
    private String titulo;

    @Column
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private EstadoTareaPersonal estado;

    @Column
    private LocalDateTime fechaAsignacion;

    @Column
    private LocalDateTime fechaLimite;
}