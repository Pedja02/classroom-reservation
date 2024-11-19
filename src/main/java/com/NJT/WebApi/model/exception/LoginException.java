package com.NJT.WebApi.model.exception;

public class LoginException extends Exception{
    private String msg;
    public LoginException(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return msg;
    }

}
