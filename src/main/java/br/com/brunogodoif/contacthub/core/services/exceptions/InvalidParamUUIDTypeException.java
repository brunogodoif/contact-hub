package br.com.brunogodoif.contacthub.core.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParamUUIDTypeException extends RuntimeException {
    public InvalidParamUUIDTypeException(String msg) {
        super(msg);
    }
}
