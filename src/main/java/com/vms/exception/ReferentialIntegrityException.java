package com.vms.exception;

public class ReferentialIntegrityException extends RuntimeException{
    public ReferentialIntegrityException(String message) {
        super(message);
    }
}
