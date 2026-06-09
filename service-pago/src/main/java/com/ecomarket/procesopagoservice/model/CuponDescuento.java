package com.ecomarket.procesopagoservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupon_descuento")
@Data @NoArgsConstructor @AllArgsConstructor
public class CuponDescuento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private Double porcentajeDescuento;
    private Double montoMaximoDescuento;
    private LocalDateTime fechaExpiracion;
    private Boolean activo;

    public Boolean esValido() {
        return activo != null && activo
                && fechaExpiracion != null
                && LocalDateTime.now().isBefore(fechaExpiracion);
    }
}
