package com.payments.regularpayments.config;

import com.payments.regularpayments.exception.FieldErrorException;
import com.payments.regularpayments.exception.PersonInnAlreadyExistsException;
import com.payments.regularpayments.exception.PersonNotFoundException;
import com.payments.regularpayments.exception.PersonPhoneNumberAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Map<String, String> response = new HashMap<>();

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleValidationNotFoundException(PersonNotFoundException e) {
        LOGGER.warn("Handle validation exception '{}': {}", e.getClass().getSimpleName(), e.getMessage());
        response.put("Exception", e.getClass().getSimpleName());
        response.put("Message", e.getMessage());
        return response;
    }

    @ExceptionHandler({PersonInnAlreadyExistsException.class, PersonPhoneNumberAlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, String> handleValidationConflictException(Exception e) {
        LOGGER.warn("Handle validation exception '{}': {}", e.getClass().getSimpleName(), e.getMessage());
        response.put("Exception", e.getClass().getSimpleName());
        response.put("Message", e.getMessage());
        return response;
    }

    @ExceptionHandler(FieldErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleValidationFieldErrorException(FieldErrorException e) {
        LOGGER.warn("Handle validation exception '{}': {}", e.getClass().getSimpleName(), e.getMessage());
        return e.getErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        objectError -> objectError.getDefaultMessage() != null ?
                                objectError.getDefaultMessage() :
                                "поле не прошло валидацию",
                        (s, a) -> s + ", " + a)
                );
    }
}
