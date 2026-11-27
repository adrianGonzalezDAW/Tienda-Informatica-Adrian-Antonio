package com.opt_daw.tienda_informatica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ComponenteDuplicadoException extends ResponseStatusException {

    public ComponenteDuplicadoException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}