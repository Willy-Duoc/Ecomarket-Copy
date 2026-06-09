package com.ecomarket.gestiontiendaservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "horario_atencion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioAtencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long sucursalId;

    @Column
    private Integer diaSemana;

    @Column
    private String horaApertura;

    @Column
    private String horaCierre;

    @Column
    private Boolean esFeriado;
}
