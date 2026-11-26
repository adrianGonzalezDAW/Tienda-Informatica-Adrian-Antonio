package com.opt_daw.tienda_informatica.dto;

public class CategoriaDto {
    private Long id;
    private String nombre;
    private String resumen;

    public CategoriaDto() {
    }

    public CategoriaDto(Long id, String nombre, String resumen) {
        this.id = id;
        this.nombre = nombre;
        this.resumen = resumen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
}
