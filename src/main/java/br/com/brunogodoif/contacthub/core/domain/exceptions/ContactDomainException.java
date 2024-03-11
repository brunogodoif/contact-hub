package br.com.brunogodoif.contacthub.core.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContactDomainException extends RuntimeException {
    public ContactDomainException(String message) {
        super(message);
    }
}
