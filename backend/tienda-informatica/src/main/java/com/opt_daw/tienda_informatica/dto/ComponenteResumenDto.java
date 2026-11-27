package com.opt_daw.tienda_informatica.dto;

import java.math.BigDecimal;

public class ComponenteResumenDto {

    private Long id;
    private String sku;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private Boolean destacado;
    private String categoriaNombre;
    private String marca;

    public ComponenteResumenDto() {
    }

    public ComponenteResumenDto(Long id, String sku, String nombre,
                                BigDecimal precio, Integer stock,
                                String imagenUrl, Boolean destacado,
                                String categoriaNombre, String marca) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.destacado = destacado;
        this.categoriaNombre = categoriaNombre;
        this.marca = marca;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Boolean getDestacado() { return destacado; }
    public void setDestacado(Boolean destacado) { this.destacado = destacado; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
}