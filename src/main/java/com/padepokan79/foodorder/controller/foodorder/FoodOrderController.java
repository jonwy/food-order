        package com.padepokan79.foodorder.controller.foodorder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.padepokan79.foodorder.dto.request.CartCheckoutRequest;
import com.padepokan79.foodorder.dto.request.CartRequest;
import com.padepokan79.foodorder.dto.request.FoodRequestDto;
import com.padepokan79.foodorder.dto.request.PageRequestDto;
import com.padepokan79.foodorder.dto.response.MessageResponse;
import com.padepokan79.foodorder.service.foodorder.FoodOrderService;



@RestController
@RequestMapping("/food-order")
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    public FoodOrderController(final FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @GetMapping("/foods/count")
    public ResponseEntity<Long> getMethodName() {
        return foodOrderService.getFoodsTotalRows();
    }
    
    
    @GetMapping("/foods")
    public ResponseEntity<MessageResponse> getAllFoods(@ModelAttribute PageRequestDto pageRequest, @ModelAttribute FoodRequestDto foodRequest) {
        return foodOrderService.getAllFoods(pageRequest, foodRequest);
    }

    @GetMapping("/foods/{food-id}")
    public ResponseEntity<MessageResponse> getFoodDetail(@PathVariable(name = "food-id") Integer foodId) {
        return foodOrderService.getFoodDetail(foodId);
    }

    @PutMapping("/foods/{foodId}/favorites")
    public ResponseEntity<MessageResponse> toogleFavorite(@PathVariable(name = "foodId") Integer foodId) {
        return foodOrderService.toggleFavorites(foodId);
    }

    @GetMapping("/foods/my-favorite-foods")
    public ResponseEntity<MessageResponse> getAllFavorite() {
        return foodOrderService.getAllFavorite();
    }

    @GetMapping("/cart")
    public ResponseEntity<MessageResponse> getAllCarts() {
        return foodOrderService.getAllCarts();
    }
    

    @PostMapping("/cart")
    public ResponseEntity<MessageResponse> addToCart(@RequestParam Integer foodId) {
        return foodOrderService.addToCart(foodId);
    }

    @PutMapping("/cart/{cart_id}")
    public ResponseEntity<MessageResponse> setFoodQuantityInCart(@PathVariable("cart_id") Integer cartId, @RequestBody CartRequest cartRequest) {
        return foodOrderService.setFoodQuantityInCart(cartId, cartRequest);
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<MessageResponse> deleteFoodFromCart(@PathVariable("cartId")Integer cartId) {
        return foodOrderService.deleteFoodFromCart(cartId);
    }

    @PostMapping("/cart/checkout")
    public ResponseEntity<MessageResponse> checkoutCart(@RequestBody CartCheckoutRequest request) {
        return foodOrderService.checkout(request);//S
        // throw new com.padepokan79.foodorder.exception.classes.Exception("Error");
    }

    @GetMapping("/cart/total")
    public ResponseEntity<Long> getCartTotal() {
        return foodOrderService.getCartTotal();
    }
    
    @GetMapping("/order-history")
    public ResponseEntity<MessageResponse> getOrderHistory() {
        return foodOrderService.orderHistory();
    }
}
