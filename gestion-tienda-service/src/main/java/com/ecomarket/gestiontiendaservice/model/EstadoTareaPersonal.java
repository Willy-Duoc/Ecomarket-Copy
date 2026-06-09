package com.ecomarket.gestiontiendaservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "estado_tarea_personal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoTareaPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;
}