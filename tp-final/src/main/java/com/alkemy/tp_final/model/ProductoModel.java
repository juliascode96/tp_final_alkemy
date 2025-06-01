package com.alkemy.tp_final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "productos")
public class ProductoModel {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;


}
