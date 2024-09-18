package com.padepokan79.foodorder.exception.classes;

public class AlreadyDeletedException extends RuntimeException{
    
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
