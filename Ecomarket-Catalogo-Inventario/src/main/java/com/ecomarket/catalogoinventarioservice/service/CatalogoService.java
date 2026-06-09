package com.ecomarket.catalogoinventarioservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomarket.catalogoinventarioservice.model.CategoriaProducto;
import com.ecomarket.catalogoinventarioservice.model.Producto;
import com.ecomarket.catalogoinventarioservice.repository.CategoriaProductoRepository;
import com.ecomarket.catalogoinventarioservice.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CatalogoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaProductoRepository categoriaRepository;

    public List<Producto> navegarCatalogo(Long categoriaId) {
        CategoriaProducto categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> buscarCoincidenciaPorNombre(String nombreCoincidencia) {
        return productoRepository.findByNombreContainingIgnoreCase(nombreCoincidencia);
    }

    public Producto consultarDetalles(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
    }

    public Producto agregarProducto(Producto nuevoProducto) {
        return productoRepository.save(nuevoProducto);
    }

    public Producto editarProducto(Long productoId, Producto nuevosDatos) {
        Producto existente = consultarDetalles(productoId);
        existente.setSku(nuevosDatos.getSku());
        existente.setNombre(nuevosDatos.getNombre());
        existente.setDescripcion(nuevosDatos.getDescripcion());
        existente.setPrecioBase(nuevosDatos.getPrecioBase());
        existente.setCategoria(nuevosDatos.getCategoria());
        existente.setEstado(nuevosDatos.getEstado());
        existente.setImagenUrl(nuevosDatos.getImagenUrl());
        return productoRepository.save(existente);
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public boolean eliminarProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) return false;
        productoRepository.deleteById(productoId);
        return true;
    }
}
