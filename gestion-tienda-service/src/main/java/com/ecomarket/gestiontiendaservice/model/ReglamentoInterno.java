package com.ecomarket.gestiontiendaservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reglamento_interno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReglamentoInterno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long sucursalId;

    @Column
    private Integer version;

    @Column
    private String tituloSeccion;

    @Column
    private String contenidoLegal;

    @Column
    private LocalDateTime fechaVigencia;
}