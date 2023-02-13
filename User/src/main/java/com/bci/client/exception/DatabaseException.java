package com.bci.client.exception;

public class DatabaseException extends Exception{

    private String message;

    public DatabaseException(String message){
        super(message);
        this.message = message;
    }
}
