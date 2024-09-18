package com.padepokan79.foodorder.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MessageUtils {
    
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String resource) {
        return messageSource.getMessage(resource, null, Locale.getDefault());
    }
}
