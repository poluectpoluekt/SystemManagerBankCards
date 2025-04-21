package com.ed.sysbankcards.advice;

import com.ed.sysbankcards.advice.response.RuntimeExceptionResponse;
import com.ed.sysbankcards.exception.customer.CustomerAlreadyRegisteredException;
import com.ed.sysbankcards.exception.customer.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    private RuntimeExceptionResponse error(RuntimeException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerAlreadyRegisteredException.class)
    private RuntimeExceptionResponse customerAlreadyRegistered(CustomerAlreadyRegisteredException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerNotFoundException.class)
    private RuntimeExceptionResponse customerNotFound(CustomerNotFoundException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> error(MethodArgumentNotValidException exception){
        Map<String,String> errorValid = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(
                errors -> {
                    String field = ((FieldError)errors).getField();
                    String errorMessage = errors.getDefaultMessage();
                    errorValid.put(field, errorMessage);
                }
        );
        return errorValid;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public RuntimeExceptionResponse handleAccessDenied(AccessDeniedException e) {
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

}
