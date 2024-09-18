package com.padepokan79.foodorder.exception.classes;

public class UnknownAuthenticationException extends RuntimeException{
    
    public UnknownAuthenticationException(String message) {
        super(message);
    }
}
