package com.padepokan79.foodorder.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.padepokan79.foodorder.dto.response.MessageResponse;
import com.padepokan79.foodorder.dto.response.MessageResponse.Meta;

public class ResponseUtil {
    
    public static ResponseEntity<MessageResponse> createResponse(HttpStatus status, String message) {
        return createResponse(status, message, null, null);
    }

    public static ResponseEntity<MessageResponse> createResponse(HttpStatus status, String message, Object data) {
        return createResponse(status, message, data, null);
    }

    public static ResponseEntity<MessageResponse> createResponse(HttpStatus status, String message, Object data, Meta meta) {
        MessageResponse response = new MessageResponse(message, status.toString(), status.value());
        if (data != null) {
            response.setData(data);
        }
        if (meta != null) {
            response.setMeta(meta);
        }
        return new ResponseEntity<>(response, status);
    }
}
