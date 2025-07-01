package com.alkemy.tp_final.controller;

import com.alkemy.tp_final.dto.ProductoDTO;
import com.alkemy.tp_final.service.IProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un producto. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductoDTO> postProducto(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.postProducto(productoDTO));
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable String id) {
        ProductoDTO producto = productoService.getById(id);
        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<List<ProductoDTO>>> getAllProductosAsync() {
        return productoService.getAllProductosAsync()
                .thenApply(ResponseEntity::ok);
    }

    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping("/paginado")
    public ResponseEntity<Page<ProductoDTO>> getPaginatedProductos(Pageable pageable) {
        return ResponseEntity.ok(productoService.getPaginatedProductos(pageable));
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por su ID. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar producto por nombre", description = "Devuelve un producto por su nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ProductoDTO> getByName(@PathVariable String nombre) {
        Optional<ProductoDTO> producto = productoService.getByName(nombre);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar productos por rango de precio", description = "Devuelve una lista de productos dentro de un rango de precios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos dentro del rango de precios"),
            @ApiResponse(responseCode = "400", description = "Rango de precios inválido")
    })
    @GetMapping("/precio")
    public ResponseEntity<List<ProductoDTO>> getByPrecioRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(productoService.getByPrecioRange(min, max));
    }

    @Operation(summary = "Buscar productos por stock", description = "Devuelve una lista de productos con stock mayor al valor especificado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos con stock mayor al valor especificado"),
            @ApiResponse(responseCode = "400", description = "Valor de stock inválido")
    })
    @GetMapping("/stock")
    public ResponseEntity<List<ProductoDTO>> getByStockGreaterThan(@RequestParam Integer stock) {
        return ResponseEntity.ok(productoService.getByStockGreaterThan(stock));
    }

    @Operation(summary = "Buscar productos por nombre parcial", description = "Devuelve una lista de productos cuyo nombre contiene el valor especificado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos encontrados"),
            @ApiResponse(responseCode = "400", description = "Nombre inválido")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> getByNombreContaining(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.getByNombreContaining(nombre));
    }

    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto por su ID. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable String id, @RequestBody ProductoDTO productoDTO) {
        ProductoDTO updated = productoService.updateProducto(id, productoDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto por su ID. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
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

    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto por su ID. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
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

    @Operation(summary = "Generar reporte de productos", description = "Genera un reporte de productos. Solo accesible para ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte comenzado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/procesar-stock")
    public ResponseEntity<Void> procesarStockPesado() {
        productoService.procesarStockPesado();
        return ResponseEntity.accepted().build(); // 202 Accepted: proceso iniciado
    }
}
