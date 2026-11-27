
package com.opt_daw.tienda_informatica.service;

import com.opt_daw.tienda_informatica.dto.*;
import com.opt_daw.tienda_informatica.enums.DireccionOrden;
import com.opt_daw.tienda_informatica.enums.OrdenarPor;
import com.opt_daw.tienda_informatica.exception.*;
import com.opt_daw.tienda_informatica.model.Categoria;
import com.opt_daw.tienda_informatica.model.Componente;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComponenteService {

    private final List<Componente> componentes = new ArrayList<>();
    private Long id = 0L;

    private final CategoriaService categoriaService;

    public ComponenteService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // LISTADOS

    public List<ComponenteResumenDto> listarActivos() {
        List<ComponenteResumenDto> lista = new ArrayList<>();

        for (Componente c : componentes) {
            if (c.getActivo()) {
                lista.add(convertirResumen(c));
            }
        }

        return lista;
    }

    public List<ComponenteDetalleDto> listarTodos() {
        List<ComponenteDetalleDto> lista = new ArrayList<>();

        for (Componente c : componentes) {
            lista.add(convertirDetalle(c));
        }

        return lista;
    }

    public ComponenteDetalleDto buscarPorId(Long id) {
        for (Componente c : componentes) {
            if (c.getId().equals(id) && c.getActivo()) {
                return convertirDetalle(c);
            }
        }

        throw new ComponenteNoEncontradoException("Componente no encontrado.");
    }

    public List<ComponenteResumenDto> listarDestacados() {
        List<ComponenteResumenDto> lista = new ArrayList<>();

        for (Componente c : componentes) {
            if (c.getActivo() && c.getDestacado()) {
                lista.add(convertirResumen(c));
            }
        }

        return lista;
    }

    public ComponenteDetalleDto buscarPorSku(String sku) {
        for (Componente c : componentes) {
            if (c.getSku().equalsIgnoreCase(sku) && c.getActivo()) {
                return convertirDetalle(c);
            }
        }

        throw new ComponenteNoEncontradoException("Componente no encontrado.");
    }

    public List<ComponenteResumenDto> listarPorCategoria(Long categoriaId) {
        List<ComponenteResumenDto> lista = new ArrayList<>();

        for (Componente c : componentes) {
            if (c.getActivo() && c.getCategoria().getId().equals(categoriaId)) {
                lista.add(convertirResumen(c));
            }
        }

        return lista;
    }

    public List<ComponenteResumenDto> listarStockBajo(int umbral) {
        List<ComponenteResumenDto> lista = new ArrayList<>();

        for (Componente c : componentes) {
            if (c.getActivo() && c.getStock() <= umbral) {
                lista.add(convertirResumen(c));
            }
        }

        return lista;
    }

    // FILTROS Y ORDENACIÓN

    public List<ComponenteResumenDto> filtrar(ComponenteFiltroDto filtro) {
        List<Componente> filtrados = new ArrayList<>();

        for (Componente c : componentes) {
            if (!c.getActivo()) {
                continue;
            }

            if (filtro.getCategoriaId() != null &&
                    !c.getCategoria().getId().equals(filtro.getCategoriaId())) {
                continue;
            }

            if (filtro.getPrecioMin() != null &&
                    c.getPrecio().compareTo(filtro.getPrecioMin()) < 0) {
                continue;
            }

            if (filtro.getPrecioMax() != null &&
                    c.getPrecio().compareTo(filtro.getPrecioMax()) > 0) {
                continue;
            }

            if (Boolean.TRUE.equals(filtro.getSoloDisponibles()) && c.getStock() <= 0) {
                continue;
            }

            if (filtro.getMarca() != null &&
                    !c.getMarca().equalsIgnoreCase(filtro.getMarca())) {
                continue;
            }

            if (filtro.getBuscar() != null && !filtro.getBuscar().isBlank()) {
                String texto = normalizar(filtro.getBuscar());
                String datos = normalizar(c.getNombre() + " " + c.getSku()
                        + " " + c.getDescripcion() + " " + c.getMarca());

                if (!datos.contains(texto)) {
                    continue;
                }
            }

            filtrados.add(c);
        }

        String orden = filtro.getOrden();
        String direccion = filtro.getDireccion();

        if (orden == null) orden = "NOMBRE";
        if (direccion == null) direccion = "ASC";

        OrdenarPor tipo;
        DireccionOrden sentido;

        try {
            tipo = OrdenarPor.valueOf(orden.toUpperCase());
            sentido = DireccionOrden.valueOf(direccion.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DatosInvalidosException("Ordenación no válida.");
        }

        // ORDENACION
        for (int i = 0; i < filtrados.size() - 1; i++) {
            for (int j = 0; j < filtrados.size() - 1 - i; j++) {
                Componente a = filtrados.get(j);
                Componente b = filtrados.get(j + 1);
                int comparacion = 0;

                if (tipo == OrdenarPor.NOMBRE) {
                    comparacion = a.getNombre().compareToIgnoreCase(b.getNombre());
                } else if (tipo == OrdenarPor.PRECIO) {
                    comparacion = a.getPrecio().compareTo(b.getPrecio());
                } else if (tipo == OrdenarPor.NOVEDAD) {
                    comparacion = a.getFechaAlta().compareTo(b.getFechaAlta());
                }

                if (sentido == DireccionOrden.DESC) {
                    comparacion = -comparacion;
                }

                if (comparacion > 0) {
                    filtrados.set(j, b);
                    filtrados.set(j + 1, a);
                }
            }
        }

        List<ComponenteResumenDto> resultado = new ArrayList<>();

        for (Componente c : filtrados) {
            resultado.add(convertirResumen(c));
        }

        return resultado;
    }

    // CREAR Y MODIFICAR

    public ComponenteDetalleDto crear(ComponenteCrearDto dto) {
        validarDatos(dto);

        String sku = dto.getSku().trim().toUpperCase();

        for (Componente c : componentes) {
            if (c.getSku().equals(sku)) {
                throw new ComponenteDuplicadoException("Ya existe un componente con ese SKU.");
            }
        }

        Categoria categoria = obtenerCategoria(dto.getCategoriaId());

        Componente c = new Componente();
        c.setId(++id);
        c.setSku(sku);
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
        c.setPrecio(dto.getPrecio());
        c.setStock(dto.getStock());
        c.setImagenUrl(dto.getImagenUrl());
        c.setActivo(true);
        c.setDestacado(dto.getDestacado() != null && dto.getDestacado());
        c.setFechaAlta(LocalDateTime.now());
        c.setCategoria(categoria);
        c.setMarca(dto.getMarca());
        c.setGarantiaMeses(dto.getGarantiaMeses());

        componentes.add(c);

        return convertirDetalle(c);
    }

    public ComponenteDetalleDto modificar(Long id, ComponenteCrearDto dto) {
        validarDatos(dto);

        Componente componente = null;

        for (Componente c : componentes) {
            if (c.getId().equals(id) && c.getActivo()) {
                componente = c;
                break;
            }
        }

        if (componente == null) {
            throw new ComponenteNoEncontradoException("Componente no encontrado.");
        }

        String sku = dto.getSku().trim().toUpperCase();

        for (Componente c : componentes) {
            if (!c.getId().equals(id) && c.getSku().equals(sku)) {
                throw new ComponenteDuplicadoException("Ya existe un componente con ese SKU.");
            }
        }

        componente.setSku(sku);
        componente.setNombre(dto.getNombre());
        componente.setDescripcion(dto.getDescripcion());
        componente.setPrecio(dto.getPrecio());
        componente.setImagenUrl(dto.getImagenUrl());
        componente.setDestacado(dto.getDestacado() != null && dto.getDestacado());
        componente.setCategoria(obtenerCategoria(dto.getCategoriaId()));
        componente.setMarca(dto.getMarca());
        componente.setGarantiaMeses(dto.getGarantiaMeses());

        return convertirDetalle(componente);
    }

    // RETIRAR, REACTIVAR Y STOCK

    public void retirar(Long id) {
        for (Componente c : componentes) {
            if (c.getId().equals(id) && c.getActivo()) {
                c.setActivo(false);
                return;
            }
        }

        throw new ComponenteNoEncontradoException("Componente no encontrado.");
    }

    public ComponenteDetalleDto reactivar(Long id) {
        for (Componente c : componentes) {
            if (c.getId().equals(id)) {
                c.setActivo(true);
                return convertirDetalle(c);
            }
        }

        throw new ComponenteNoEncontradoException("Componente no encontrado.");
    }

    public ComponenteDetalleDto actualizarStock(Long id, StockDto dto) {
        for (Componente c : componentes) {
            if (c.getId().equals(id)) {
                if (dto.getCantidad() == null ||
                        c.getStock() + dto.getCantidad() < 0) {
                    throw new DatosInvalidosException("El stock no puede ser negativo.");
                }

                c.setStock(c.getStock() + dto.getCantidad());
                return convertirDetalle(c);
            }
        }

        throw new ComponenteNoEncontradoException("Componente no encontrado.");
    }

    public boolean existePorCategoriaId(Long categoriaId) {
        for (Componente c : componentes) {
            if (c.getCategoria().getId().equals(categoriaId)) {
                return true;
            }
        }

        return false;
    }

    // MÉTODOS AUXILIARES

    private void validarDatos(ComponenteCrearDto dto) {
        if (dto.getSku() == null || dto.getSku().isBlank() ||
                dto.getNombre() == null || dto.getNombre().isBlank() ||
                dto.getPrecio() == null || dto.getPrecio().compareTo(BigDecimal.ZERO) < 0 ||
                dto.getStock() == null || dto.getStock() < 0 ||
                dto.getCategoriaId() == null ||
                dto.getGarantiaMeses() != null && dto.getGarantiaMeses() < 0) {
            throw new DatosInvalidosException("Los datos del componente no son válidos.");
        }
    }

    private Categoria obtenerCategoria(Long id) {
        CategoriaDto dto = categoriaService.buscarCategoria(id);

        return new Categoria(dto.getId(), dto.getNombre(), dto.getDescripcion());
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private ComponenteResumenDto convertirResumen(Componente c) {
        return new ComponenteResumenDto(
                c.getId(), c.getSku(), c.getNombre(), c.getPrecio(),
                c.getStock(), c.getImagenUrl(), c.getDestacado(),
                c.getCategoria().getNombre(), c.getMarca()
        );
    }

    private ComponenteDetalleDto convertirDetalle(Componente c) {
        return new ComponenteDetalleDto(
                c.getId(), c.getSku(), c.getNombre(), c.getDescripcion(),
                c.getPrecio(), c.getStock(), c.getImagenUrl(), c.getActivo(),
                c.getDestacado(), c.getFechaAlta(), c.getCategoria().getId(),
                c.getCategoria().getNombre(), c.getMarca(), c.getGarantiaMeses()
        );
    }
}
