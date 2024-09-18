package com.padepokan79.foodorder.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.padepokan79.foodorder.model.FavoriteFood;
import com.padepokan79.foodorder.model.FavoriteFoodId;
import com.padepokan79.foodorder.model.Food;
import com.padepokan79.foodorder.model.User;

@Repository
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, FavoriteFoodId>{

    @Query("SELECT CASE WHEN COUNT(fv) > 0 THEN TRUE ELSE FALSE END FROM FavoriteFood fv WHERE fv.user= :user AND fv.food= :food")
    Boolean existsFavoriteFoodsByFoodIdAndUserId(@Param("food") Food food, @Param("user") User user);

    @Query("SELECT fv FROM FavoriteFood fv WHERE fv.user= :user AND fv.food= :food")
    Collection<FavoriteFood> findFavoriteFoodByFoodIdAndUserId(@Param("food") Food food, @Param("user") User user);

    default void updateOrInsert(FavoriteFood favoriteFood) {
        save(favoriteFood);
    }
}
