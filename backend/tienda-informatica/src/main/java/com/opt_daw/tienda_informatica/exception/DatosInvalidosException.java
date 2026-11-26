package com.opt_daw.tienda_informatica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DatosInvalidosException extends ResponseStatusException {
    public DatosInvalidosException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
