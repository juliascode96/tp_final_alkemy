package com.alkemy.tp_final.repository;

import com.alkemy.tp_final.model.ProductoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductoRepository extends MongoRepository<ProductoModel, String> {

}
