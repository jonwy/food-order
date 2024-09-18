package com.padepokan79.foodorder.service.projection;

import com.padepokan79.foodorder.model.Category;

public interface FoodWithFavorite {
    public Integer getFoodId();
    public String getFoodName();
    public Category getCategory();
    public Integer getPrice();
    public String getImageFilename();
    public String getIngredient();
    public Boolean getIsFavorite();
    public Boolean getIsOnCart();
}
