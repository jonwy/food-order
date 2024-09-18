package com.padepokan79.foodorder.exception.classes;

import java.util.Map;

public class ValidationException extends RuntimeException{

    private Map<String, String> fieldErrors;

    public ValidationException(Map<String, String> fieldErrors) {
        super("Kolom tidak boleh kosong!");
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(String field, String error, String message) {
        super(message);
        
    }

    public boolean hasErrorFields() {
        return fieldErrors != null || !fieldErrors.isEmpty();
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
    

}
