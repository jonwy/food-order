package com.padepokan79.foodorder.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private Integer cartId;
    private FoodDto food;
    private UserDto user;
    private Integer quantity;
    private boolean isDeleted;
    private Date createdTime;
    private Date modifiedTime;
    private String createdBy;
    private String modifiedBy;
}
