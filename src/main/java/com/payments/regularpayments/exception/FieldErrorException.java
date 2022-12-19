package com.payments.regularpayments.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class FieldErrorException extends Exception{
    private final List<FieldError> errors;

    public FieldErrorException(List<FieldError> errors, String message) {
        super(message);
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
