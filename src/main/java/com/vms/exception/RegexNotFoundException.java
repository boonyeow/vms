package com.vms.exception;

public class RegexNotFoundException extends RuntimeException{
    public RegexNotFoundException(String message) {
        super(message);
    }
}
