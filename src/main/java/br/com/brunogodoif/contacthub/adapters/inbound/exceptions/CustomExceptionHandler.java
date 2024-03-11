package br.com.brunogodoif.contacthub.adapters.inbound.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Status padrão para exceções não tratadas

        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
            status = ex.getClass().getAnnotation(ResponseStatus.class).value(); // Obtém o status da exceção anotada
        }

        CustomErrorResponse response = new CustomErrorResponse(
                Instant.now().toEpochMilli(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(response, status);
    }

    // Classe para representar o formato da resposta personalizada
    static class CustomErrorResponse {
        private final long timestamp;
        private final int status;
        private final String error;
        private final String message;
        private final String path;

        public CustomErrorResponse(long timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }
    }
}
