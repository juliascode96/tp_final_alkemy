package com.alkemy.tp_final.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta tras autenticación exitosa")
public class AuthResponse {

    @Schema(description = "JWT generado para el usuario autenticado", example = "eyJhbGciOiJIUzI1...")
    private String token;
}
