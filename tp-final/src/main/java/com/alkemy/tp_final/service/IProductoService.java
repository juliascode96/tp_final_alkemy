package com.alkemy.tp_final.service;

import com.alkemy.tp_final.dto.ProductoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IProductoService {
    ProductoDTO postProducto(ProductoDTO productoDTO);
    ProductoDTO getById(String id);
    List<ProductoDTO> getAllProductos();
    void deleteProducto(String id);
    Optional<ProductoDTO> getByName(String nombre);
    List<ProductoDTO> getByPrecioRange(Double min, Double max);
    List<ProductoDTO> getByStockGreaterThan(Integer stock);
    List<ProductoDTO> getByNombreContaining(String nombre);
    ProductoDTO updateProducto(String id, ProductoDTO productoDTO);
    ProductoDTO reduceStock(String id, Integer cantidad);
}
