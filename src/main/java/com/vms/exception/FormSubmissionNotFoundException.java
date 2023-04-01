package com.vms.exception;

public class FormSubmissionNotFoundException extends RuntimeException{
    public FormSubmissionNotFoundException(String message) {
        super(message);
    }
}
