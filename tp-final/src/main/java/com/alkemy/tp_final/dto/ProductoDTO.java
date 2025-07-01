package com.alkemy.tp_final.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Datos de un producto del sistema")
public class ProductoDTO {

    @Schema(description = "ID del producto", example = "64ef70c5ae1fbb0021d9c123")
    private String id;

    @Schema(description = "Nombre del producto", example = "Camiseta roja")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Camiseta de algodón, talla M")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "1999.99")
    private Double precio;

    @Schema(description = "Stock disponible del producto", example = "20")
    private Integer stock;

    public ProductoDTO(String number, String arrozIntegral, double v, int i) {
        this.id = number;
        this.nombre = arrozIntegral;
        this.precio = v;
        this.stock = i;
    }
}

