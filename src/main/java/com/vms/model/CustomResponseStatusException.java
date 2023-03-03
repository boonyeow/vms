package com.vms.model;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomResponseStatusException extends ResponseStatusException {

    public CustomResponseStatusException(HttpStatus status) {
        super(status);
    }

    public CustomResponseStatusException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public CustomResponseStatusException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

}
