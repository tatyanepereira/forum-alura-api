package com.forumalura.infra.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException (String msg){
        super(msg);
    }
}
