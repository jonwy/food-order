package com.padepokan79.foodorder.exception.classes;

public class DataNotFoundException extends RuntimeException{
    
    public DataNotFoundException(String message) {
        super(message);
    }
}
