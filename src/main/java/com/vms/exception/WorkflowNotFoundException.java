package com.vms.exception;

public class WorkflowNotFoundException extends RuntimeException{
    public WorkflowNotFoundException(String message) {
        super(message);
    }

}
