package com.padepokan79.foodorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequestDto {
    
    private String foodName = "%%";
    private Integer categoryId;
    private Integer price;
}
