package com.padepokan79.foodorder.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.padepokan79.foodorder.dto.FoodProjection;
import com.padepokan79.foodorder.model.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer>{
    
    Page<Food> findAll(Pageable pageable);
    @Query("SELECT f.foodId as foodId, f.foodName as foodName, f.category as category, f.price as price, f.imageFilename as imageFilename," +
        " CASE WHEN ff.food is NOT NULL AND (ff.user.userId = :userId AND ff.isFavorite=true) THEN true ELSE false END as isFavorite" +
        " FROM Food f" + 
        " LEFT JOIN FavoriteFood ff ON f.foodId = ff.food.foodId AND ff.user.userId = :userId")
    Page<FoodProjection> findAllWithFavorite(Pageable pageable, @Param("userId") Integer userId);

    @Query("""
            SELECT f.foodId as foodId, f.foodName as foodName, f.category as category, f.price as price, f.imageFilename as imageFilename,
                CASE
                    WHEN ff.food IS NOT NULL AND (ff.user.userId = :userId AND ff.isFavorite=true) THEN true ELSE false END as isFavorite,
                CASE
                    WHEN c.cartId IS NOT NULL AND (c.user.userId = :userId AND c.food.foodId = f.foodId AND c.isDeleted = false)
                        THEN true ELSE false END AS isOnCart
            FROM Food f
            LEFT JOIN FavoriteFood ff ON ff.food.foodId = f.foodId AND ff.user.userId = :userId
            LEFT JOIN Cart c ON c.user.userId = :userId AND c.food.foodId = f.foodId
            """)
    Page<FoodProjection> findAllCustomViewByUserId(Pageable pageable, @Param("userId") Integer userId);

    @Query("""
            SELECT f.foodId as foodId, f.foodName as foodName, 
                f.category as category, f.price as price, 
                f.imageFilename as imageFilename, f.ingredient as ingredient, 
                CASE 
                    WHEN ff.food IS NOT NULL AND (ff.isFavorite = true and ff.user.userId = :userId)  THEN true
                        ELSE false
                END AS isFavorite,
                CASE
                    WHEN c.cartId IS NOT NULL AND (c.user.userId = :userId AND c.food.foodId = f.foodId AND c.isDeleted = false)
                        THEN true ELSE false END AS isOnCart
            FROM Food f
            LEFT JOIN Cart c ON c.food.foodId = f.foodId AND c.user.userId = :userId
            LEFT JOIN FavoriteFood ff ON ff.food.foodId = f.foodId AND ff.user.userId = :userId
            WHERE f.foodId = :foodId
            """)
    Optional<FoodProjection> findById(@Param("foodId") Integer foodId, @Param("userId") Integer userId);

    @Query("""
            SELECT f.foodId as foodId, f.foodName as foodName, 
                f.category as category, f.price as price, 
                f.imageFilename as imageFilename, f.ingredient as ingredient, 
                CASE 
                    WHEN ff.food IS NOT NULL AND (ff.isFavorite = true)  THEN true
                    ELSE false
                END
                AS isFavorite,
                CASE
                    WHEN c.cartId IS NOT NULL AND (c.food.foodId = f.foodId AND c.isDeleted = false)
                        THEN true ELSE false END AS isOnCart
            FROM Food f
            LEFT JOIN FavoriteFood ff ON ff.food.foodId = f.foodId AND ff.user.userId = :userId
            LEFT JOIN Cart c ON c.user.userId = :userId AND c.food.foodId = f.foodId
            WHERE ff.isFavorite = true ORDER BY f.foodName asc
            """)
    Page<FoodProjection> findAllFavorite(Pageable pageable, @Param("userId") Integer userId);
    

    // main method
    @Query(value = """
        SELECT f.foodId as foodId, f.foodName as foodName, 
            f.category as category, f.price as price, 
            f.imageFilename as imageFilename, f.ingredient as ingredient,
            f.createdTime as createdTime, f.modifiedTime as modifiedTime,
            f.createdBy as createdBy, f.modifiedBy as modifiedBy,
            CASE 
                WHEN ff.food IS NOT NULL AND ff.isFavorite = true  THEN true
                ELSE false
            END AS isFavorite,
            CASE
                WHEN c.cartId IS NOT NULL AND (c.food.foodId = f.foodId AND c.isDeleted = false)
                    THEN true ELSE false END AS isOnCart
        FROM Food f
        LEFT JOIN FavoriteFood ff ON ff.food.foodId = f.foodId AND ff.user.userId = :userId
        LEFT JOIN Cart c ON c.user.userId = :userId AND c.food.foodId = f.foodId
        WHERE (:foodName IS NULL OR LOWER(f.foodName) LIKE LOWER(:foodName))
            AND (:categoryId IS NULL OR f.category.categoryId = :categoryId)
    """)
    Page<FoodProjection> findAll(
            @Param("foodName")String foodName,
            @Param("categoryId")Integer categoryId,
            @Param("userId")Integer userId,
            
            Pageable pageable);
}
