package br.com.brunogodoif.contacthub.core.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContactUpdatedException extends RuntimeException {
    public ContactUpdatedException(String msg) {
        super(msg);
    }
}