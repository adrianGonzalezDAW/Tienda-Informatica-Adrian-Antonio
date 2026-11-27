
package com.opt_daw.tienda_informatica.controller;

import com.opt_daw.tienda_informatica.dto.ComponenteCrearDto;
import com.opt_daw.tienda_informatica.dto.ComponenteDetalleDto;
import com.opt_daw.tienda_informatica.dto.ComponenteFiltroDto;
import com.opt_daw.tienda_informatica.dto.ComponenteResumenDto;
import com.opt_daw.tienda_informatica.dto.StockDto;
import com.opt_daw.tienda_informatica.service.ComponenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ComponenteController {

    private final ComponenteService componenteService;

    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ComponenteResumenDto>> listarProductos(@ModelAttribute ComponenteFiltroDto filtro) {
        return ResponseEntity.ok(componenteService.filtrar(filtro));
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ComponenteDetalleDto> buscarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(componenteService.buscarPorId(id));
    }

    @GetMapping("/productos/destacados")
    public ResponseEntity<List<ComponenteResumenDto>> listarDestacados() {
        return ResponseEntity.ok(componenteService.listarDestacados());
    }

    @GetMapping("/admin/productos")
    public ResponseEntity<List<ComponenteDetalleDto>> listarProductosAdmin() {
        return ResponseEntity.ok(componenteService.listarTodos());
    }

    @PostMapping("/admin/productos")
    public ResponseEntity<ComponenteDetalleDto> crearProducto(@RequestBody ComponenteCrearDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(componenteService.crear(dto));
    }

    @PutMapping("/admin/productos/{id}")
    public ResponseEntity<ComponenteDetalleDto> modificarProducto(
            @RequestBody ComponenteCrearDto dto,
            @PathVariable Long id) {
        return ResponseEntity.ok(componenteService.modificar(id, dto));
    }

    @DeleteMapping("/admin/productos/{id}")
    public ResponseEntity<Void> retirarProducto(@PathVariable Long id) {
        componenteService.retirar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/productos/{id}/stock")
    public ResponseEntity<ComponenteDetalleDto> actualizarStock(
            @PathVariable Long id,
            @RequestBody StockDto dto) {
        return ResponseEntity.ok(componenteService.actualizarStock(id, dto));
    }

    @PutMapping("/admin/productos/{id}/reactivar")
    public ResponseEntity<ComponenteDetalleDto> reactivarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(componenteService.reactivar(id));
    }
}
