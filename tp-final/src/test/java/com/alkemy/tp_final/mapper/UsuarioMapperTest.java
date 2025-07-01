package com.alkemy.tp_final.mapper;

import com.alkemy.tp_final.dto.UsuarioDTO;
import com.alkemy.tp_final.model.UsuarioModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private final UsuarioMapper usuarioMapper = new UsuarioMapper();

    @Test
    void toDTO_MapsCorrectly() {
        UsuarioModel model = new UsuarioModel();
        model.setId("123");
        model.setEmail("test@mail.com");
        model.setPassword("secret");
        model.setRoles(List.of("USER"));
        model.setActivo(true);

        UsuarioDTO dto = usuarioMapper.toDTO(model);

        assertEquals("123", dto.getId());
        assertEquals("test@mail.com", dto.getEmail());
        assertEquals("secret", dto.getPassword());
        assertEquals(List.of("USER"), dto.getRoles());
        assertTrue(dto.getActivo());
    }

    @Test
    void toModel_MapsCorrectly() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId("456");
        dto.setEmail("admin@mail.com");
        dto.setPassword("admin123");
        dto.setRoles(List.of("ADMIN"));
        dto.setActivo(false);

        UsuarioModel model = usuarioMapper.toModel(dto);

        assertEquals("456", model.getId());
        assertEquals("admin@mail.com", model.getEmail());
        assertEquals("admin123", model.getPassword());
        assertEquals(List.of("ADMIN"), model.getRoles());
        assertFalse(model.getActivo());
    }
}
