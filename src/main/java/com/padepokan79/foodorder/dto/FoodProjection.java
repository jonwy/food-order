package com.padepokan79.foodorder.dto;

import com.padepokan79.foodorder.model.Category;

public interface FoodProjection {
    public Integer getFoodId();
    public String getFoodName();
    public Category getCategory();
    public Integer getPrice();
    public String getImageFilename();
    public String getIngredient();
    public Boolean getIsFavorite();
    public Boolean getIsOnCart();
}
