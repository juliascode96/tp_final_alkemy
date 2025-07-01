package com.alkemy.tp_final.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos del usuario del sistema")
public class UsuarioDTO {

    @Schema(description = "ID del usuario", example = "64ef70c5ae1fbb0021d9c456")
    private String id;

    @Email
    @Schema(description = "Email del usuario", example = "admin@mail.com")
    private String email;

    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Schema(description = "Contraseña del usuario", example = "12345678")
    private String password;

    @Schema(description = "Roles asignados al usuario", example = "[\"ADMIN\", \"USER\"]")
    private List<String> roles;

    @Schema(description = "Estado de activación del usuario", example = "true")
    private Boolean activo;
}
