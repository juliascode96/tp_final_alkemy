package com.alkemy.tp_final.service.impl;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.mapper.ProductoMapper;
import com.alkemy.tp_final.model.ProductoModel;
import com.alkemy.tp_final.repository.ProductoRepository;
import com.alkemy.tp_final.service.IProductoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@AllArgsConstructor
@Service
public class ProductoService implements IProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final ExecutorService taskExecutor;

    @Override
    public ProductoDTO postProducto(ProductoDTO productoDTO) {
        ProductoModel savedProducto = productoRepository.save(productoMapper.toModel(productoDTO));
        return productoMapper.toDTO(savedProducto);
    }

    @Override
    public ProductoDTO getById(String id) {
        return productoRepository.findById(id)
                .map(productoMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public CompletableFuture<List<ProductoDTO>> getAllProductosAsync() {
        return CompletableFuture.supplyAsync(() ->
            productoRepository.findAll().stream()
                .map(productoMapper::toDTO)
                .toList(),
            taskExecutor
        )
        .thenApply(productos -> {
            System.out.println("Productos recuperados exitosamente. Cantidad: " + productos.size());
            return productos;
        })
        .exceptionally(ex -> {
            System.err.println("Error al obtener productos: " + ex.getMessage());
            ex.printStackTrace();
            return List.of();
        });
    }

    @Override
    public Page<ProductoDTO> getPaginatedProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(productoMapper::toDTO);
    }

    @Override
    public void deleteProducto(String id) {
        productoRepository.deleteById(id);
    }

    @Override
    public Optional<ProductoDTO> getByName(String nombre) {
        return productoRepository.findByNombreIgnoreCase(nombre).stream()
                .findFirst()
                .map(productoMapper::toDTO);
    }

    @Override
    public List<ProductoDTO> getByPrecioRange(Double min, Double max) {
        return productoRepository.findByPrecioBetween(min, max).stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoDTO> getByStockGreaterThan(Integer stock) {
        return productoRepository.findByStockGreaterThan(stock).stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoDTO> getByNombreContaining(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public ProductoDTO updateProducto(String id, ProductoDTO productoDTO) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setNombre(productoDTO.getNombre());
                    existingProducto.setPrecio(productoDTO.getPrecio());
                    existingProducto.setStock(productoDTO.getStock());
                    ProductoModel updatedProducto = productoRepository.save(existingProducto);
                    return productoMapper.toDTO(updatedProducto);
                })
                .orElse(null);
    }

    @Override
    public ProductoDTO reduceStock(String id, Integer cantidad) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    if (existingProducto.getStock() >= cantidad) {
                        existingProducto.setStock(existingProducto.getStock() - cantidad);
                        ProductoModel updatedProducto = productoRepository.save(existingProducto);
                        return productoMapper.toDTO(updatedProducto);
                    } else {
                        throw new IllegalArgumentException("Stock insuficiente para reducir");
                    }
                })
                .orElse(null);
    }

    @Override
    public ProductoDTO addStock(String id, Integer cantidad) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setStock(existingProducto.getStock() + cantidad);
                    ProductoModel updatedProducto = productoRepository.save(existingProducto);
                    return productoMapper.toDTO(updatedProducto);
                })
                .orElse(null);
    }

    @Override
    public CompletableFuture<Void> procesarStockPesado() {
        return CompletableFuture.runAsync(() -> {
            List<ProductoModel> productos = productoRepository.findAll();
            // Simulamos procesamiento pesado:
            productos.forEach(p -> {
                try {
                    Thread.sleep(100); // Simula un procesamiento pesado de 100 ms por producto
                    System.out.println("Procesado producto: " + p.getNombre());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            // Luego de procesar, se genera un CSV con los productos:
            StringBuilder csvBuilder = new StringBuilder();
            csvBuilder.append("ID,Nombre,Precio,Stock\n");
            for (ProductoModel producto : productos) {
                csvBuilder.append(producto.getId()).append(",");
                csvBuilder.append("\"").append(producto.getNombre()).append("\",");
                csvBuilder.append(producto.getPrecio()).append(",");
                csvBuilder.append(producto.getStock()).append("\n");
            }

            // Guardar el CSV en disco
            try (FileOutputStream fos = new FileOutputStream("productos_procesados.csv")) {
                fos.write(csvBuilder.toString().getBytes(StandardCharsets.UTF_8));
                System.out.println("CSV generado exitosamente.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, taskExecutor);
    }
}

