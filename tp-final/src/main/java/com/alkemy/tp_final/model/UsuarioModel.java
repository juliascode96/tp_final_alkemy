package com.alkemy.tp_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "usuarios")
public class UsuarioModel {
    @Id
    private String id;
    private String email;
    private String password;
    private List<String> roles; // ADMIN, USER, etc.
    private Boolean activo;
}
