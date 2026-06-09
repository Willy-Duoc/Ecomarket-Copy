package com.ecomarket.envioservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomarket.envioservice.model.entity.HistorialEnvio;
import com.ecomarket.envioservice.repository.HistorialEnvioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HistorialEnvioService {

    @Autowired
    private HistorialEnvioRepository historialEnvioRepository;

    public List<HistorialEnvio> findHistorialByEnvioId(Long envioId) {
        return historialEnvioRepository.findByEnvioIdOrderByFechaActualizacionAsc(envioId);
    }

    public HistorialEnvio save(HistorialEnvio historialEnvio) {
        return historialEnvioRepository.save(historialEnvio);
    }
}
