package com.NJT.WebApi.model.exception;

public class EmailFailureException extends Exception{
    public EmailFailureException(String message){
        super(message);
    }
}
