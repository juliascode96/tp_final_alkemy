package com.alkemy.tp_final.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductoDTO {

    private String id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
}
