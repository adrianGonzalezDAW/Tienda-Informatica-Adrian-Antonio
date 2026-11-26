package com.opt_daw.tienda_informatica.service;

import com.opt_daw.tienda_informatica.dto.CategoriaCrearDto;
import com.opt_daw.tienda_informatica.dto.CategoriaDto;
import com.opt_daw.tienda_informatica.exception.CategoriaDuplicadaException;
import com.opt_daw.tienda_informatica.exception.CategoriaNoEncontradaException;
import com.opt_daw.tienda_informatica.exception.DatosInvalidosException;
import com.opt_daw.tienda_informatica.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {
    // Lista de categorias en memoria
    private List<Categoria> categorias = new ArrayList<>();
    private Long id = 0L;

    public List<CategoriaDto> listarCategorias() {
        // Lista que vamos a devolver
        List<CategoriaDto> categoriaDtos = new ArrayList<>();

        // Bucle para recorrer categorias y pasarlo a la lista de dtos
        for (Categoria categoria : categorias) {
            categoriaDtos.add(new CategoriaDto(categoria.getId(), categoria.getNombre(), categoria.getDescripcion()));
        }

        return categoriaDtos;
    }

    public CategoriaDto buscarCategoria(Long id) {
        // Categoria a devolver
        CategoriaDto categoriaDto = null;
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id)) {
                categoriaDto = new CategoriaDto(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
            }
        }

        // Si no existe la categoria lanzamos la excepcion
        if (categoriaDto == null) {
            throw new CategoriaNoEncontradaException("No se ha encontrado la categoria indicada.");
        }

        return categoriaDto;
    }

    public CategoriaDto crearCategoria(CategoriaCrearDto categoriaCrearDto) {
        // Controlamos si es null el objeto en si el nombre si esta en blanco
        if (categoriaCrearDto == null || categoriaCrearDto.getNombre() == null || categoriaCrearDto.getNombre().isBlank()) {
            throw new DatosInvalidosException("Datos invalidos al crear la categoria.");
        }

        // Si no es null cogemos el nombre y la descripcion
        String nombre = categoriaCrearDto.getNombre().trim();
        String descripcion = categoriaCrearDto.getDescripcion();

        // Revisamos la descripcion, si es nula se queda nula, si esta en blanco se queda nula y sino pues se le pone. Yo defino descripcion como opcional y no obligatorio
        if (descripcion != null) {
            descripcion = descripcion.trim();
            if (descripcion.isBlank()) {
                descripcion = null;
            }
        }

        for (Categoria categoria : categorias) {
            // Si existe otra categoria con el mismo nombre lanzamos la excepcion
            if (categoria.getNombre().equalsIgnoreCase(nombre)) {
                throw new CategoriaDuplicadaException("Ya existe una categoria con ese nombre.");
            }
        }

        // Añadimos la categoria a la lista
        Categoria categoria = new Categoria(++id, nombre, descripcion);
        categorias.add(categoria);

        // Devolvemos la categoria creada
        return new CategoriaDto(id, nombre, descripcion);
    }

    public CategoriaDto modificarCategoria(Long id, CategoriaCrearDto categoriaCrearDto) {
        if (categoriaCrearDto == null) {
            throw new DatosInvalidosException("Datos invalidos al modificar la categoria.");
        }

        Categoria categoriaEncontrada = null;
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id)) {
                categoriaEncontrada = categoria;
                break;
            }
        }

        // Si no existe la categoria lanzamos la excepcion
        if (categoriaEncontrada == null) {
            throw new CategoriaNoEncontradaException("No se ha encontrado la categoria indicada.");
        }

        // Controlamos si es null el nombre o esta en blanco
        if (categoriaCrearDto.getNombre() == null || categoriaCrearDto.getNombre().isBlank()) {
            throw new DatosInvalidosException("Datos invalidos al modificar la categoria.");
        }

        String nombre = categoriaCrearDto.getNombre().trim();
        String descripcion = categoriaCrearDto.getDescripcion();

        // Comprobamos la descripcion
        if (descripcion != null) {
            descripcion = descripcion.trim();
            if (descripcion.isBlank()) {
                descripcion = null;
            }
        }

        // Revisamos que no hay una categoria con el nuevo nombre
        for (Categoria categoria : categorias) {
            if (!categoria.getId().equals(id) && categoria.getNombre().equalsIgnoreCase(nombre)) {
                throw new CategoriaDuplicadaException("Ya existe una categoria con ese nombre.");
            }
        }

        // Actualizamos la categoria
        categoriaEncontrada.setNombre(nombre);
        categoriaEncontrada.setDescripcion(descripcion);

        // Devolvemos la categoria modificada
        return new CategoriaDto(id, nombre, descripcion);
    }

    public void eliminarCategoria(Long id) {
        Categoria categoriaEncontrada = null;
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id)) {
                categoriaEncontrada = categoria;
                break;
            }
        }

        // Si no existe la categoria lanzamos la excepcion
        if (categoriaEncontrada == null) {
            throw new CategoriaNoEncontradaException("No se ha encontrado la categoria indicada.");
        }

        // TODO: Implementar que mire si la categoria tiene productos no se elimine

        categorias.remove(categoriaEncontrada);
    }
}
