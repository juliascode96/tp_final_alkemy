package com.alkemy.tp_final.controller;


import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.service.IProductoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private IProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @Test
    @DisplayName("postProducto - creación exitosa")
    void postProducto_ReturnsProductoDTO() {
        ProductoDTO input = new ProductoDTO(null, "Pan", "Fresco", 10.0, 100);
        ProductoDTO output = new ProductoDTO("1", "Pan", "Fresco", 10.0, 100);
        when(productoService.postProducto(input)).thenReturn(output);

        ResponseEntity<ProductoDTO> response = productoController.postProducto(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pan", response.getBody().getNombre());
    }

    @Test
    @DisplayName("getById - producto encontrado")
    void getById_ReturnsProducto() {
        ProductoDTO producto = new ProductoDTO("1", "Leche", "Entera", 8.5, 50);
        when(productoService.getById("1")).thenReturn(producto);

        ResponseEntity<ProductoDTO> response = productoController.getById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Leche", response.getBody().getNombre());
    }

    @Test
    @DisplayName("getById - producto no encontrado")
    void getById_ReturnsNotFound() {
        when(productoService.getById("999")).thenReturn(null);

        ResponseEntity<ProductoDTO> response = productoController.getById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("getAllProductos - retorna lista")
    void getAllProductos_ReturnsList() {
        List<ProductoDTO> productos = List.of(new ProductoDTO("1", "Arroz", "", 5.0, 20));
        when(productoService.getAllProductos()).thenReturn(productos);

        ResponseEntity<List<ProductoDTO>> response = productoController.getAllProductos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("deleteProducto - eliminación exitosa")
    void deleteProducto_ReturnsNoContent() {
        ResponseEntity<Void> response = productoController.deleteProducto("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productoService).deleteProducto("1");
    }

    @Test
    @DisplayName("getByNombreContaining - resultado exitoso")
    void getByNombreContaining_ReturnsList() {
        List<ProductoDTO> list = List.of(new ProductoDTO("1", "Pan Integral", "", 12.0, 30));
        when(productoService.getByNombreContaining("pan")).thenReturn(list);

        ResponseEntity<List<ProductoDTO>> response = productoController.getByNombreContaining("pan");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("reduceStock - stock suficiente")
    void reduceStock_ReducesSuccessfully() {
        ProductoDTO producto = new ProductoDTO("1", "Harina", "", 20.0, 80);
        when(productoService.reduceStock("1", 10)).thenReturn(producto);

        ResponseEntity<ProductoDTO> response = productoController.reduceStock("1", 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(80, response.getBody().getStock());
    }

    @Test
    @DisplayName("reduceStock - stock insuficiente")
    void reduceStock_ReturnsBadRequest() {
        when(productoService.reduceStock("1", 999)).thenThrow(new IllegalArgumentException());

        ResponseEntity<ProductoDTO> response = productoController.reduceStock("1", 999);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
