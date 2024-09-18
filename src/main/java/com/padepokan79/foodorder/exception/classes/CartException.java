package com.padepokan79.foodorder.exception.classes;

public class CartException extends RuntimeException{
    
    public CartException(String message) {
        super("Error: " + message);
    }
}
