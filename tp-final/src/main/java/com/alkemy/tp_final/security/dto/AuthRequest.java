package com.alkemy.tp_final.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para autenticación del usuario")
public class AuthRequest {

    @Schema(description = "Email del usuario", example = "usuario@mail.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "12345678")
    private String password;
}

