package com.alkemy.tp_final.service.impl;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.mapper.ProductoMapper;
import com.alkemy.tp_final.model.ProductoModel;
import com.alkemy.tp_final.repository.ProductoRepository;
import com.alkemy.tp_final.service.IProductoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductoService implements IProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    public ProductoDTO postProducto(ProductoDTO productoDTO) {
        ProductoModel savedProducto = productoRepository.save(productoMapper.toModel(productoDTO));
        return productoMapper.toDTO(savedProducto);
    }

    @Override
    public ProductoDTO getById(String id) {
        return productoRepository.findById(id)
                .map(productoMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public Page<ProductoDTO> getPaginatedProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(productoMapper::toDTO);
    }

    @Override
    public void deleteProducto(String id) {
        productoRepository.deleteById(id);
    }

    @Override
    public Optional<ProductoDTO> getByName(String nombre) {
        return productoRepository.findByNombreIgnoreCase(nombre).stream()
                .findFirst()
                .map(productoMapper::toDTO);
    }

    @Override
    public List<ProductoDTO> getByPrecioRange(Double min, Double max) {
        return productoRepository.findByPrecioBetween(min, max).stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoDTO> getByStockGreaterThan(Integer stock) {
        return productoRepository.findByStockGreaterThan(stock).stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoDTO> getByNombreContaining(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public ProductoDTO updateProducto(String id, ProductoDTO productoDTO) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setNombre(productoDTO.getNombre());
                    existingProducto.setPrecio(productoDTO.getPrecio());
                    existingProducto.setStock(productoDTO.getStock());
                    ProductoModel updatedProducto = productoRepository.save(existingProducto);
                    return productoMapper.toDTO(updatedProducto);
                })
                .orElse(null);
    }

    @Override
    public ProductoDTO reduceStock(String id, Integer cantidad) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    if (existingProducto.getStock() >= cantidad) {
                        existingProducto.setStock(existingProducto.getStock() - cantidad);
                        ProductoModel updatedProducto = productoRepository.save(existingProducto);
                        return productoMapper.toDTO(updatedProducto);
                    } else {
                        throw new IllegalArgumentException("Stock insuficiente para reducir");
                    }
                })
                .orElse(null);
    }

    @Override
    public ProductoDTO addStock(String id, Integer cantidad) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setStock(existingProducto.getStock() + cantidad);
                    ProductoModel updatedProducto = productoRepository.save(existingProducto);
                    return productoMapper.toDTO(updatedProducto);
                })
                .orElse(null);
    }
}
