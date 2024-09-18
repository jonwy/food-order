package com.padepokan79.foodorder.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    
    private String message;
    private String status;
    private Integer statusCode;
}
