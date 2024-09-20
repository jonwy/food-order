package com.padepokan79.foodorder.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodDto {

    private Integer foodId;
    private CategoryDto category;
    private String foodName;
    private String imageFilename;
    private String ingredient;
    private Integer price;
    private Date createdTime;
    private Date modifiedTime;
    private String createdBy;
    private String modifiedBy;
    private Boolean isFavorite;
    private Boolean isOnCart;
}
