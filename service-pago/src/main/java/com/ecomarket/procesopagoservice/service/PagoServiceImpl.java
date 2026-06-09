package com.ecomarket.procesopagoservice.service;

import com.ecomarket.procesopagoservice.model.*;
import com.ecomarket.procesopagoservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementación de PagoService.
 *
 * Puntos de integración con otros microservicios (a implementar cuando se unan):
 *
 *  - carrito-compra-service  → iniciarPago: obtener montos reales del carrito
 *                            → procesarConTransbank: cerrar carrito tras pago aprobado
 *
 *  - registro-usuarios-service → generarFactura: obtener nombre del cliente
 *                              → enviarBoletaEmail: obtener email si no se pasa uno
 *                              → procesarConTransbank: obtener dirección para el envío
 *
 *  - logistica-envios-service → procesarConTransbank: crear envío tras pago aprobado
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final TransaccionRepository transaccionRepository;
    private final FacturaRepository facturaRepository;
    private final CuponRepository cuponRepository;
    private final EstadoPagoRepository estadoPagoRepository;

    @Override
    public TransaccionPago iniciarPago(Long pedidoId, Long clienteId, Double monto, MetodoPagoTransaccion metodo) {
        EstadoPago estadoPendiente = estadoPagoRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no encontrado"));

        // TODO: consultar carrito-compra-service para obtener subtotal/descuento/total reales

        TransaccionPago transaccion = new TransaccionPago();
        transaccion.setPedidoId(pedidoId);
        transaccion.setClienteId(clienteId);
        transaccion.setMontoSubtotal(monto);
        transaccion.setMontoDescuento(0.0);
        transaccion.setMontoTotal(monto);
        transaccion.setMetodoPago(metodo);
        transaccion.setEstado(estadoPendiente);

        return transaccionRepository.save(transaccion);
    }

    @Override
    public TransaccionPago anadirCuponDescuento(Long transaccionId, Long cuponId) {
        TransaccionPago transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada: " + transaccionId));

        CuponDescuento cupon = cuponRepository.findById(cuponId)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado: " + cuponId));

        if (!cupon.esValido()) {
            throw new RuntimeException("El cupón no es válido o está expirado");
        }

        Double descuento = transaccion.getMontoSubtotal() * (cupon.getPorcentajeDescuento() / 100.0);
        if (cupon.getMontoMaximoDescuento() != null) {
            descuento = Math.min(descuento, cupon.getMontoMaximoDescuento());
        }

        transaccion.setMontoDescuento(descuento);
        transaccion.setMontoTotal(transaccion.getMontoSubtotal() - descuento);
        transaccion.setCuponUtilizadoId(cuponId);

        return transaccionRepository.save(transaccion);
    }

    @Override
    public TransaccionPago procesarConTransbank(Long transaccionId, String token) {
        TransaccionPago transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada: " + transaccionId));

        EstadoPago estadoAprobado = estadoPagoRepository.findByNombre("APROBADO")
                .orElseThrow(() -> new RuntimeException("Estado APROBADO no encontrado"));

        transaccion.setTokenTransbank(token);
        transaccion.setCodigoAutorizacion(LocalDateTime.now());
        transaccion.setEstado(estadoAprobado);

        // TODO: cerrar carrito en carrito-compra-service
        // TODO: obtener dirección del cliente en registro-usuarios-service
        // TODO: crear envío en logistica-envios-service

        return transaccionRepository.save(transaccion);
    }

    @Override
    public Boolean procesarReembolso(Long transaccionId, String motivo) {
        TransaccionPago transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada: " + transaccionId));

        EstadoPago estadoReembolsado = estadoPagoRepository.findByNombre("REEMBOLSADO")
                .orElseThrow(() -> new RuntimeException("Estado REEMBOLSADO no encontrado"));

        transaccion.setEstado(estadoReembolsado);
        transaccionRepository.save(transaccion);

        // TODO: POST a tabla/servicio de REMBOLSOS con motivo
        log.info("Reembolso procesado para transacción {}: {}", transaccionId, motivo);
        return true;
    }

    @Override
    public FacturaElectronica generarFactura(Long transaccionId, Long rut, String giro) {
        TransaccionPago transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada: " + transaccionId));

        // TODO: obtener nombre real del cliente desde registro-usuarios-service

        FacturaElectronica factura = new FacturaElectronica();
        factura.setTransaccionId(transaccionId);
        factura.setClienteId(transaccion.getClienteId());
        factura.setRutReceptor(String.valueOf(rut));
        factura.setRazonSocial(giro);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setFolioFiscal(System.currentTimeMillis());
        factura.setXmlDocumento("<factura><transaccion>" + transaccionId
                + "</transaccion><monto>" + transaccion.getMontoTotal() + "</monto></factura>");

        return facturaRepository.save(factura);
    }

    @Override
    public Boolean enviarBoletaEmail(Long transaccionId, String correoDestino) {
        transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada: " + transaccionId));

        // TODO: si correoDestino viene vacío, obtenerlo desde registro-usuarios-service
        // TODO: integrar con servicio de email (SendGrid, SES, JavaMail)
        log.info("Enviando boleta de transacción {} a {}", transaccionId, correoDestino);
        return true;
    }

    @Override
    public TransaccionPago obtenerTransaccion(Long transaccionId) {
        return transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada: " + transaccionId));
    }
}
