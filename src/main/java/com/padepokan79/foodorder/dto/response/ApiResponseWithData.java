package com.padepokan79.foodorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class ApiResponseWithData extends ApiResponse{

    public ApiResponseWithData(String message, String status, Integer statusCode, Integer total, Object data) {
        super(message, status, statusCode);
        this.data = data;
        this.total = total;
    }
    
    private Integer total;
    private Object data;
    
}
        