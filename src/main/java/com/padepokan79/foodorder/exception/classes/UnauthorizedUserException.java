package com.padepokan79.foodorder.exception.classes;

public class UnauthorizedUserException extends RuntimeException {
    
    public UnauthorizedUserException(String message) {
        super(message);
    }
}
