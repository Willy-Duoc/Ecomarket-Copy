package com.ecomarket.procesopagoservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion_pago")
@Data @NoArgsConstructor @AllArgsConstructor
public class TransaccionPago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private Double montoSubtotal;
    private Double montoDescuento;
    private Double montoTotal;

    @ManyToOne
    @JoinColumn(name = "metodo_pago_id")
    private MetodoPagoTransaccion metodoPago;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private EstadoPago estado;

    private Long cuponUtilizadoId;
    private String tokenTransbank;
    private LocalDateTime codigoAutorizacion;
}
