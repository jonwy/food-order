package com.padepokan79.foodorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {

    private Integer orderDetailId;
    private Integer quantity;
    private Integer totalPrice;
    private OrderDetailFoodDto food;
}
