package br.com.brunogodoif.contacthub.adapters.outbound.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Log4j2
public class DatabaseAccessException extends RuntimeException {
    public DatabaseAccessException(Exception e) {
        super(e);
        log.error(this);
    }
}