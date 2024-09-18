package com.padepokan79.foodorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailFoodDto {
    private Integer foodId;
    private String foodName;
    private String imageFilename;
    private Integer price;
}
