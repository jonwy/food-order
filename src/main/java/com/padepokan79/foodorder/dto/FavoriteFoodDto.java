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
public class FavoriteFoodDto {

    private boolean isFavorite;
    private UserDto userDto;
    private FoodDto foodDto;
    private Date createdTime;
    private Date modifiedTime;
    private String modifiedBy;
    private String createdBy;
}
