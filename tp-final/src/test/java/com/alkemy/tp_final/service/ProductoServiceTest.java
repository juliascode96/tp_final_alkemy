package com.alkemy.tp_final.service;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.mapper.ProductoMapper;
import com.alkemy.tp_final.model.ProductoModel;
import com.alkemy.tp_final.repository.ProductoRepository;
import com.alkemy.tp_final.service.impl.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock private ProductoRepository productoRepository;
    @Mock private ProductoMapper productoMapper;
    @Mock private ExecutorService executorService;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void postProducto_CreatesProducto() {
        ProductoDTO dto = new ProductoDTO(null, "Pan", "", 10.0, 50);
        ProductoModel model = new ProductoModel();
        ProductoModel saved = new ProductoModel();
        ProductoDTO expected = new ProductoDTO("1", "Pan", "", 10.0, 50);

        when(productoMapper.toModel(dto)).thenReturn(model);
        when(productoRepository.save(model)).thenReturn(saved);
        when(productoMapper.toDTO(saved)).thenReturn(expected);

        ProductoDTO result = productoService.postProducto(dto);

        assertEquals(expected, result);
    }

    @Test
    void getById_ReturnsProducto_WhenExists() {
        ProductoModel model = new ProductoModel();
        ProductoDTO dto = new ProductoDTO("1", "Arroz", "", 5.0, 20);

        when(productoRepository.findById("1")).thenReturn(Optional.of(model));
        when(productoMapper.toDTO(model)).thenReturn(dto);

        ProductoDTO result = productoService.getById("1");

        assertEquals("Arroz", result.getNombre());
    }

    @Test
    void getById_ReturnsNull_WhenNotFound() {
        when(productoRepository.findById("99")).thenReturn(Optional.empty());

        assertNull(productoService.getById("99"));
    }

    @Test
    void deleteProducto_CallsRepository() {
        productoService.deleteProducto("1");
        verify(productoRepository).deleteById("1");
    }

    @Test
    void reduceStock_ThrowsException_WhenInsufficientStock() {
        ProductoModel producto = new ProductoModel();
        producto.setStock(5);
        when(productoRepository.findById("1")).thenReturn(Optional.of(producto));

        assertThrows(IllegalArgumentException.class, () -> productoService.reduceStock("1", 10));
    }

    @Test
    void reduceStock_ReducesStock_WhenSufficient() {
        ProductoModel producto = new ProductoModel();
        producto.setStock(20);
        ProductoModel saved = new ProductoModel();
        ProductoDTO expected = new ProductoDTO("1", "Pan", "", 10.0, 10);

        when(productoRepository.findById("1")).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(saved);
        when(productoMapper.toDTO(saved)).thenReturn(expected);

        ProductoDTO result = productoService.reduceStock("1", 10);

        assertEquals(10, result.getStock());
    }

    @Test
    void getByName_ReturnsProducto_WhenExists() {
        ProductoModel model = new ProductoModel();
        ProductoDTO dto = new ProductoDTO("1", "Harina", "", 8.0, 15);
        ProductoModel producto = new ProductoModel();
        when(productoRepository.findByNombreIgnoreCase("harina")).thenReturn(Optional.of(producto));
        when(productoMapper.toDTO(model)).thenReturn(dto);

        Optional<ProductoDTO> result = productoService.getByName("harina");

        assertTrue(result.isPresent());
        assertEquals("Harina", result.get().getNombre());
    }

    @Test
    void getByName_ReturnsProducto_WhenNonExists() {
        when(productoRepository.findByNombreIgnoreCase("noexiste")).thenReturn(Optional.empty());

        Optional<ProductoDTO> result = productoService.getByName("noexiste");

        assertFalse(result.isPresent());
    }

    @Test
    void getByStockGreaterThan_ReturnsProducto_WhenExists() {
        ProductoModel model = new ProductoModel();
        ProductoDTO dto = new ProductoDTO("1", "Harina", "", 8.0, 15);
        ProductoModel producto = new ProductoModel();
        when(productoRepository.findByStockGreaterThan(1)).thenReturn(List.of(producto));
        when(productoMapper.toDTO(model)).thenReturn(dto);

        List<ProductoDTO> result = productoService.getByStockGreaterThan(1);

        assertFalse(result.isEmpty());
        assertEquals("Harina", result.get(0).getNombre());
        assertTrue(result.get(0).getStock() > 1);
    }

    @Test
    void getByStockGreaterThan_ReturnsProducto_WhenNonExists() {
        when(productoRepository.findByStockGreaterThan(200)).thenReturn(List.of());
        List<ProductoDTO> result = productoService.getByStockGreaterThan(200);
        assertTrue(result.isEmpty());
    }

    @Test
    void getByPrecioRange_ReturnsProductos_WhenExists() {
        ProductoModel model = new ProductoModel();
        ProductoDTO dto = new ProductoDTO("1", "Harina", "", 8.0, 15);
        when(productoRepository.findByPrecioBetween(5.0, 10.0)).thenReturn(List.of(model));
        when(productoMapper.toDTO(model)).thenReturn(dto);

        List<ProductoDTO> result = productoService.getByPrecioRange(5.0, 10.0);

        assertFalse(result.isEmpty());
        assertEquals("Harina", result.get(0).getNombre());
    }

    @Test
    void getByPrecioRange_ReturnsProducto_WhenNonExists() {
        when(productoRepository.findByPrecioBetween(5.0, 10.0)).thenReturn(List.of());
        List<ProductoDTO> result = productoService.getByPrecioRange(5.0, 10.0);
        assertTrue(result.isEmpty());
    }

    @Test
    void addStock() {
        ProductoModel producto = new ProductoModel();
        producto.setStock(20);
        ProductoModel saved = new ProductoModel();
        ProductoDTO expected = new ProductoDTO("1", "Pan", "", 10.0, 30);

        when(productoRepository.findById("1")).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenReturn(saved);
        when(productoMapper.toDTO(saved)).thenReturn(expected);

        ProductoDTO result = productoService.addStock("1", 10);

        assertEquals(30, result.getStock());
    }

    @Test
    void updateProducto() {
        ProductoModel existing = new ProductoModel();
        existing.setNombre("Arroz");
        existing.setPrecio(5.0);
        existing.setStock(20);

        ProductoDTO updateDto = new ProductoDTO("1", "Arroz Integral", 6.0, 25);
        ProductoModel updatedModel = new ProductoModel();
        updatedModel.setNombre("Arroz Integral");
        updatedModel.setPrecio(6.0);
        updatedModel.setStock(25);

        when(productoRepository.findById("1")).thenReturn(Optional.of(existing));
        when(productoRepository.save(existing)).thenReturn(updatedModel);
        when(productoMapper.toDTO(updatedModel)).thenReturn(updateDto);

        ProductoDTO result = productoService.updateProducto("1", updateDto);

        assertEquals("Arroz Integral", result.getNombre());
        assertEquals(6.0, result.getPrecio());
        assertEquals(25, result.getStock());
    }

    @Test
    void updateProducto_ReturnsNull_WhenNotFound() {
        ProductoDTO updateDto = new ProductoDTO("99", "Arroz Integral", 6.0, 25);
        when(productoRepository.findById("99")).thenReturn(Optional.empty());
        ProductoDTO result = productoService.updateProducto("99", updateDto);
        assertNull(result);
    }

    @Test
    void getByNombreContaining_ReturnsProductos_WhenExists() {
        ProductoModel model = new ProductoModel();
        ProductoDTO dto = new ProductoDTO("1", "Pan Integral", "", 12.0, 30);
        when(productoRepository.findByNombreContainingIgnoreCase("pan")).thenReturn(List.of(model));
        when(productoMapper.toDTO(model)).thenReturn(dto);

        List<ProductoDTO> result = productoService.getByNombreContaining("pan");

        assertFalse(result.isEmpty());
        assertEquals("Pan Integral", result.get(0).getNombre());
    }

    @Test
    void getByNombreContaining_ReturnsProductos_WhenNonExists() {
        when(productoRepository.findByNombreContainingIgnoreCase("noexiste")).thenReturn(List.of());
        List<ProductoDTO> result = productoService.getByNombreContaining("noexiste");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllProductos_ReturnsList() {
        ProductoModel model1 = new ProductoModel();
        List<ProductoModel> models = List.of(model1);
        when(productoRepository.findAll()).thenReturn(models);

        ProductoDTO dto1 = new ProductoDTO("1", "Producto 1", "", 10.0, 50);
        when(productoMapper.toDTO(model1)).thenReturn(dto1);

        List<ProductoDTO> result = productoService.getAllProductos();

        assertEquals(1, result.size());
        assertEquals("Producto 1", result.get(0).getNombre());
    }

}

