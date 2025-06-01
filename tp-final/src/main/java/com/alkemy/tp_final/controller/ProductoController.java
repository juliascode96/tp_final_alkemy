package com.alkemy.tp_final.controller;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.service.IProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> postProducto(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.postProducto(productoDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable String id) {
        ProductoDTO producto = productoService.getById(id);
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ProductoDTO> getByName(@PathVariable String nombre) {
        Optional<ProductoDTO> producto = productoService.getByName(nombre);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/precio")
    public ResponseEntity<List<ProductoDTO>> getByPrecioRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(productoService.getByPrecioRange(min, max));
    }

    @GetMapping("/stock")
    public ResponseEntity<List<ProductoDTO>> getByStockGreaterThan(@RequestParam Integer stock) {
        return ResponseEntity.ok(productoService.getByStockGreaterThan(stock));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> getByNombreContaining(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.getByNombreContaining(nombre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable String id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO updated = productoService.updateProducto(id, productoDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/reducir-stock")
    public ResponseEntity<ProductoDTO> reduceStock(@PathVariable String id, @RequestParam Integer cantidad) {
        try {
            ProductoDTO producto = productoService.reduceStock(id, cantidad);
            return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
