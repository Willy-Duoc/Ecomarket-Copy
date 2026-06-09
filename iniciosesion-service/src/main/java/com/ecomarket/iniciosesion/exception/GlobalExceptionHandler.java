package com.ecomarket.iniciosesion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Map<String, Object> buildError(HttpStatus status, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        return body;
    }

    private Map<String, Object> buildErrorConCampos(HttpStatus status, String mensaje, List<String> campos) {
        Map<String, Object> body = buildError(status, mensaje);
        body.put("campos", campos);
        return body;
    }

    // ── Validación de entrada (@Valid) ────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildErrorConCampos(HttpStatus.BAD_REQUEST, "Error de validación en los datos enviados", errores));
    }

    // ── Dominio ───────────────────────────────────────────────────────────────

    @ExceptionHandler(CredencialNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCredencialNotFound(CredencialNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(AutenticacionException.class)
    public ResponseEntity<Map<String, Object>> handleAutenticacion(AutenticacionException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(CuentaBloqueadaException.class)
    public ResponseEntity<Map<String, Object>> handleCuentaBloqueada(CuentaBloqueadaException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildError(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleTokenInvalido(TokenInvalidoException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleCorreoDuplicado(CorreoDuplicadoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // ── Fallback ──────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error interno del servidor. Contacte al administrador."));
    }
}
