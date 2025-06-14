package com.alkemy.tp_final.repository;

import com.alkemy.tp_final.model.ProductoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends MongoRepository<ProductoModel, String> {
    Page<ProductoModel> findAll(Pageable pageable);
    List<ProductoModel> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoModel> findByPrecioBetween(Double min, Double max);
    List<ProductoModel> findByStockGreaterThan(Integer stock);
    Optional<ProductoModel> findByNombreIgnoreCase(String nombre);
}
