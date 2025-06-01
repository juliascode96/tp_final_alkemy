package com.alkemy.tp_final.mapper;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.model.ProductoModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public ProductoDTO toDTO(ProductoModel productoModel) {
        return modelMapper.map(productoModel, ProductoDTO.class);
    }

    public ProductoModel toModel(ProductoDTO productoDTO) {
        return modelMapper.map(productoDTO, ProductoModel.class);
    }
}
