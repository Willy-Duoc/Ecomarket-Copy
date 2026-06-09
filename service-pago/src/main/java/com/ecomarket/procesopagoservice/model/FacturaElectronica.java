package com.ecomarket.procesopagoservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura_electronica")
@Data @NoArgsConstructor @AllArgsConstructor
public class FacturaElectronica {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long folioFiscal;
    private Long transaccionId;
    private Long clienteId;
    private String rutReceptor;
    private String razonSocial;
    private String xmlDocumento;
    private LocalDateTime fechaEmision;
}
