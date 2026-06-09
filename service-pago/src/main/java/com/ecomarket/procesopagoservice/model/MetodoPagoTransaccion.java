package com.ecomarket.procesopagoservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "metodo_pago_transaccion")
@Data @NoArgsConstructor @AllArgsConstructor
public class MetodoPagoTransaccion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
}
