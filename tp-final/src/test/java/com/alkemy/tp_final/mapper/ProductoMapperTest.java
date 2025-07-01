package com.alkemy.tp_final.mapper;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.model.ProductoModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductoMapperTest {

    private final ProductoMapper productoMapper = new ProductoMapper();

    @Test
    void toDTO_MapsCorrectly() {
        ProductoModel model = new ProductoModel();
        model.setId("p1");
        model.setNombre("Coca Cola");
        model.setDescripcion("Bebida gaseosa");
        model.setPrecio(200.0);
        model.setStock(30);

        ProductoDTO dto = productoMapper.toDTO(model);

        assertEquals("p1", dto.getId());
        assertEquals("Coca Cola", dto.getNombre());
        assertEquals("Bebida gaseosa", dto.getDescripcion());
        assertEquals(200.0, dto.getPrecio());
        assertEquals(30, dto.getStock());
    }

    @Test
    void toModel_MapsCorrectly() {
        ProductoDTO dto = new ProductoDTO("p2", "Pepsi", "Gaseosa", 180.0, 40);

        ProductoModel model = productoMapper.toModel(dto);

        assertEquals("p2", model.getId());
        assertEquals("Pepsi", model.getNombre());
        assertEquals("Gaseosa", model.getDescripcion());
        assertEquals(180.0, model.getPrecio());
        assertEquals(40, model.getStock());
    }
}
