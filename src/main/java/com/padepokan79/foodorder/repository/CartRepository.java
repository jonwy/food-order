package com.padepokan79.foodorder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.padepokan79.foodorder.model.Cart;
import com.padepokan79.foodorder.model.Food;
import com.padepokan79.foodorder.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{

    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.isDeleted= FALSE")
    List<Cart> findNotDeletedCartByUserId(@Param("user") User user);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Cart c WHERE c.user = :user AND c.food = :food AND c.isDeleted= FALSE")
    Boolean existIsNotDeletedByUserIdAndFoodId(@Param("food") Food food, @Param("user")User user);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Cart c WHERE c.user = :user AND c.food = :food")
    Boolean existsByFoodIdAndUserId(@Param("food") Food food, @Param("user")User user);

    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.food = :food")
    Optional<Cart> findByFoodIdAndUserId(@Param("food") Food food, @Param("user")User user);

    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.food = :food AND c.isDeleted= FALSE")
    List<Cart> findNotDeletedCartByUserIdAndFoodId(@Param("food") Food food, @Param("user")User user);

    @Query("SELECT c FROM Cart c WHERE c.food= :food AND c.cartId= :cartId")
    List<Cart> findByCartIdAndFoodId(@Param("cartId")Integer cartId, @Param("food") Food food);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user.userId = :userId AND c.isDeleted = false")
    Long countByUserId(@Param("userId") Integer userId);

}
