package com.bci.client.exception;

public class BusinessException extends Exception{

    private String message;

    public BusinessException(String message){
        super(message);
        this.message = message;
    }


}
