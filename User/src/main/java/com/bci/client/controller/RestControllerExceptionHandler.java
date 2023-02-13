package com.bci.client.controller;

import com.bci.client.exception.BusinessException;
import com.bci.client.exception.DatabaseException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestControllerExceptionHandler{

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        List<String> exceptions = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        errors.put("timestamp",timestamp.toString());
        errors.put("code", Integer.toString(HttpStatus.BAD_REQUEST.value()));
        ex.getBindingResult().getAllErrors().forEach(e->{
            exceptions.add(e.getDefaultMessage());
        });
        String collect = exceptions.stream().
                collect(Collectors.joining("  -  "));
        errors.put("detail", collect);
        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BusinessException.class)
    public Map<String,String> handleBusinessException(
            BusinessException ex) {
        return setErrors(ex);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Map<String,String> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException ex) {
        return setErrors(ex);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException.class)
    public Map<String,String> handleBadCredentialsException(
            BadCredentialsException ex) {
        return setErrors(ex);
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SignatureException.class)
    public Map<String,String> handleSignatureException(
            SignatureException ex) {
        return setErrors(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DatabaseException.class)
    public Map<String,String> handleDatabaseException(
            DatabaseException ex) {
        return setErrors(ex);
    }

    private Map<String,String> setErrors(Exception ex){
        Map<String,String> errors = new HashMap<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        errors.put("timestamp",timestamp.toString());
        errors.put("code", Integer.toString(HttpStatus.CONFLICT.value()));
        errors.put("detail", ex.getMessage());
        return errors;
    }
}
