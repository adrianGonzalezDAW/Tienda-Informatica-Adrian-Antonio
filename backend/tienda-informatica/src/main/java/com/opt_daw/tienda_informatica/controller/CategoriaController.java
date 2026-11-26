package com.opt_daw.tienda_informatica.controller;

import com.opt_daw.tienda_informatica.dto.CategoriaCrearDto;
import com.opt_daw.tienda_informatica.dto.CategoriaDto;
import com.opt_daw.tienda_informatica.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDto>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDto> buscarCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarCategoria(id));
    }

    @GetMapping("/admin/categorias")
    public ResponseEntity<List<CategoriaDto>> listarCategoriasAdmin() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @PostMapping("/admin/categorias")
    public ResponseEntity<CategoriaDto> crearCategoria(@RequestBody CategoriaCrearDto categoriaCrearDto) {
        return ResponseEntity.ok(categoriaService.crearCategoria(categoriaCrearDto));
    }

    @PutMapping("/admin/categorias/{id}")
    public ResponseEntity<CategoriaDto> modificarCategoria(
            @RequestBody CategoriaCrearDto categoriaCrearDto,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(categoriaService.modificarCategoria(id, categoriaCrearDto));
    }

    @DeleteMapping("/admin/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}