package com.opt_daw.tienda_informatica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class CategoriaNoEncontradaException extends ResponseStatusException {
    public CategoriaNoEncontradaException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
