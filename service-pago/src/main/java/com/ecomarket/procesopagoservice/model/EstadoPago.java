package com.ecomarket.procesopagoservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "estado_pago")
@Data @NoArgsConstructor @AllArgsConstructor
public class EstadoPago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
}
