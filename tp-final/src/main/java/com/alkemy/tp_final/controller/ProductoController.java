package com.alkemy.tp_final.controller;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.service.IProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/paginado")
    public ResponseEntity<Page<ProductoDTO>> getPaginatedProductos(Pageable pageable) {
        return ResponseEntity.ok(productoService.getPaginatedProductos(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable String id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO updated = productoService.updateProducto(id, productoDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/{id}/reducir-stock")
    public ResponseEntity<ProductoDTO> reduceStock(@PathVariable String id, @RequestParam Integer cantidad) {
        try {
            ProductoDTO producto = productoService.reduceStock(id, cantidad);
            return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/aumentar-stock")
    public ResponseEntity<ProductoDTO> addStock(@PathVariable String id, @RequestParam Integer cantidad) {
        try {
            ProductoDTO producto = productoService.addStock(id, cantidad);
            return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
