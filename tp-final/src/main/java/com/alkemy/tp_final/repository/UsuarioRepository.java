package com.alkemy.tp_final.repository;

import com.alkemy.tp_final.model.UsuarioModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, String> {
    Optional<UsuarioModel> findByEmail(String email);
}
