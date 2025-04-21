package com.ed.sysbankcards.advice;

import com.ed.sysbankcards.advice.response.RuntimeExceptionResponse;
import com.ed.sysbankcards.exception.card.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CardExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CardWithNumberAlreadyExistsException.class)
    private RuntimeExceptionResponse cardWithNumberAlreadyExists(CardWithNumberAlreadyExistsException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CardWithNumberNoExistsException.class)
    private RuntimeExceptionResponse cardWithNumberNoExists(CardWithNumberNoExistsException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CardByIdNotExistException.class)
    private RuntimeExceptionResponse cardByIdNotExist(CardByIdNotExistException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientFundsException.class)
    private RuntimeExceptionResponse insufficientFunds(InsufficientFundsException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CardBlockedException.class)
    private RuntimeExceptionResponse cardBlocked(CardBlockedException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LimitExhaustedException.class)
    private RuntimeExceptionResponse limitExhausted(LimitExhaustedException e){
        return new RuntimeExceptionResponse(e.getMessage(), LocalDateTime.now());
    }
}
