package com.nitrowise.basesrvr.exception;

import org.springframework.validation.BindingResult;

public class ApiException extends RuntimeException {

    private final transient BindingResult errors;

    public ApiException() {
        this.errors = null;
    }

    public ApiException(BindingResult errors) {
        this.errors = errors;
    }

    public ApiException(String message) {
        super(message);
        this.errors = null;
    }

    public ApiException(String message, BindingResult errors) {
        super(message);
        this.errors = errors;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.errors = null;
    }

    public boolean hasErrors() {
        return this.errors != null;
    }

    public BindingResult getErrors() {
        return this.errors;
    }

}
