package com.alkemy.tp_final.mapper;

import com.alkemy.tp_final.dto.UsuarioDTO;
import com.alkemy.tp_final.model.UsuarioModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public UsuarioDTO toDTO(UsuarioModel usuarioModel) {
        return modelMapper.map(usuarioModel, UsuarioDTO.class);
    }

    public UsuarioModel toModel(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, UsuarioModel.class);
    }
}
