package com.ecomarket.catalogoinventarioservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventario_stock")
public class InventarioStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Long sucursalId;

    @Column(nullable = false)
    private Integer cantidadDisponible;

    private Integer cantidadReservada = 0;

    private Integer stockMinimoAlerta;

    private LocalDateTime ultimaReposicion;

    public boolean hayStock(Integer cantidadSolicitada) {
        return this.cantidadDisponible >= cantidadSolicitada;
    }
}
