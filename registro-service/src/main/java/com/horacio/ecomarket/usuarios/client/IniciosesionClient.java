package com.horacio.ecomarket.usuarios.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class IniciosesionClient {

    private final RestTemplate restTemplate;

    private static final String CREDENCIAL_URL = "http://localhost:8086/api/sesion/credencial";

    /**
     * Notifica al iniciosesion-service para que cree la credencial
     * del usuario recién registrado.
     *
     * @param usuarioId  ID generado tras guardar en usuarios_db
     * @param correo     Correo del usuario
     * @param contrasena Contraseña ya hasheada (BCrypt)
     */
    public void crearCredencial(Long usuarioId, String correo, String contrasena) {
        Map<String, Object> body = new HashMap<>();
        body.put("usuarioId", usuarioId);
        body.put("correo", correo);
        body.put("contrasena", contrasena);

        try {
            restTemplate.postForObject(CREDENCIAL_URL, body, Object.class);
            log.info("Credencial creada en iniciosesion-service para usuarioId={}", usuarioId);
        } catch (Exception e) {
            log.error("Error al crear credencial en iniciosesion-service para usuarioId={}: {}",
                    usuarioId, e.getMessage());
            throw new RuntimeException("No se pudo sincronizar la credencial con el servicio de login.", e);
        }
    }
}