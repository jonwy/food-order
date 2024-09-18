package com.padepokan79.foodorder.service.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.padepokan79.foodorder.dto.request.FoodRequestDto;
import com.padepokan79.foodorder.model.Cart;
import com.padepokan79.foodorder.model.FavoriteFood;
import com.padepokan79.foodorder.model.Food;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class FoodSpecification {

    public static Specification<Food> foodFilter(FoodRequestDto requestDto) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), false);
            predicates.add(isDeletedPredicate);

            if (requestDto.getFoodName() != null) {
                String foodNameValue = "%" + requestDto.getFoodName().toLowerCase() + "%";
                Predicate foodNamePredicates = criteriaBuilder.like(criteriaBuilder.lower(root.get("foodName")), foodNameValue);
                predicates.add(foodNamePredicates);
            }
            if (requestDto.getCategoryId() != null) {
                Predicate categoryIdPredicate = criteriaBuilder.equal(root.get("category").get("categoryId"), requestDto.getCategoryId());
                predicates.add(categoryIdPredicate);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        };
    }

    public static Specification<Food> findAllFavorite(FoodRequestDto requestDto, Integer userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Food, FavoriteFood> favoriteFoodJoin = root.join("favoriteFoods", JoinType.LEFT);
            Join<Food, Cart> cartJoin = root.join("carts", JoinType.LEFT);
            if (requestDto.getFoodName() != null) {
                String foodNameValue = "%" + requestDto.getFoodName().toLowerCase() + "%";
                Predicate foodNamePredicates = criteriaBuilder.like(criteriaBuilder.lower(favoriteFoodJoin.get("food").get("foodName")), foodNameValue);
                predicates.add(foodNamePredicates);
            }
            if (requestDto.getCategoryId() != null) {
                Predicate categoryIdPredicate = criteriaBuilder.equal(favoriteFoodJoin.get("food").get("category").get("categoryId"), requestDto.getCategoryId());
                predicates.add(categoryIdPredicate);
            }
            predicates.add(criteriaBuilder.equal(favoriteFoodJoin.get("isFavorite"), true));
            query.where(predicates.toArray(new Predicate[0]));
            query.multiselect(
              root.get("foodId"),  
              root.get("foodName"),  
              root.get("category"),  
              root.get("price"),  
              root.get("imageFilename"),  
              root.get("ingredient"), 
              criteriaBuilder.selectCase()
                        .when(criteriaBuilder.and(
                            criteriaBuilder.isNotNull(favoriteFoodJoin.get("food")),
                            criteriaBuilder.isTrue(favoriteFoodJoin.get("isFavorite")),
                            criteriaBuilder.equal(favoriteFoodJoin.get("user").get("userId"), userId)
                        ), true)
                        .otherwise(false)
                        .as(Boolean.class),
              criteriaBuilder.selectCase()
                        .when(criteriaBuilder.and(
                            criteriaBuilder.isNotNull(cartJoin.get("cartId")),
                            criteriaBuilder.isTrue(cartJoin.get("isDeleted")),
                            criteriaBuilder.equal(cartJoin.get("food").get("foodId"), root.get("foodId")),
                            criteriaBuilder.equal(cartJoin.get("user").get("userId"), userId)
                        ), true)
                        .otherwise(false)
                        .as(Boolean.class)
            );
            
            query.orderBy(criteriaBuilder.asc(root.get("foodName")));
            return query.getRestriction();
        };
    }
    
}
