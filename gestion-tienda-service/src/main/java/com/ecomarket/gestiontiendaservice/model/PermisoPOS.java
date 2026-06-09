package com.ecomarket.gestiontiendaservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "permiso_pos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoPOS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rolEmpleado;

    private Boolean permiteAnulaciones;

    private Boolean permiteAperturaCaja;

    private Boolean permiteAplicarDescuentoManual;
}