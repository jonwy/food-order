package com.padepokan79.foodorder.dto.error;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorsDto {
    
    private Map<String, String> errors;

    // private String message;
}
