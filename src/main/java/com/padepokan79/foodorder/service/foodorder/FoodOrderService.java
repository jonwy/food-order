package com.padepokan79.foodorder.service.foodorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.padepokan79.foodorder.dto.FoodDto;
import com.padepokan79.foodorder.dto.OrderDetailDto;
import com.padepokan79.foodorder.dto.OrderHistoryDto;
import com.padepokan79.foodorder.dto.request.CartCheckoutRequest;
import com.padepokan79.foodorder.dto.request.CartRequest;
import com.padepokan79.foodorder.dto.request.FoodRequestDto;
import com.padepokan79.foodorder.dto.request.PageRequestDto;
import com.padepokan79.foodorder.dto.response.ApiResponseWithData;
import com.padepokan79.foodorder.exception.classes.DataNotFoundException;
import com.padepokan79.foodorder.exception.classes.Exception;
import com.padepokan79.foodorder.model.FavoriteFood;
import com.padepokan79.foodorder.model.Food;
import com.padepokan79.foodorder.model.Order;
import com.padepokan79.foodorder.model.OrderDetail;
import com.padepokan79.foodorder.model.User;
import com.padepokan79.foodorder.repository.FavoriteFoodRepository;
import com.padepokan79.foodorder.repository.FoodRepository;
import com.padepokan79.foodorder.repository.OrderDetailRepository;
import com.padepokan79.foodorder.repository.OrderRepository;
import com.padepokan79.foodorder.repository.UserRepository;
import com.padepokan79.foodorder.security.service.UserDetailsImplement;
import com.padepokan79.foodorder.service.projection.FoodWithFavorite;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FoodOrderService {

    private final CartService cartService;
    
    private final FavoriteFoodRepository favoriteFoodRepository;
    private final FoodRepository foodRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    

    public FoodOrderService(
        final FavoriteFoodRepository favoriteFoodRepository, 
        final CartService cartService, final FoodRepository foodRepository, 
        final ModelMapper modelMapper, final OrderDetailRepository orderDetailRepository,
        final OrderRepository orderRepository, final UserRepository userRepository
    ) {
        this.favoriteFoodRepository = favoriteFoodRepository;
        this.cartService = cartService;
        this.foodRepository = foodRepository;
        this.modelMapper = modelMapper;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Long> getFoodsTotalRows() {
        return ResponseEntity.ok().body(foodRepository.count());
    }

    public ApiResponseWithData getAllFoods(PageRequestDto pageRequest, FoodRequestDto foodRequestDto) {
        HttpStatus status = HttpStatus.OK;
        
        int userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        
        Pageable pageable = PageRequest.of(
                                pageRequest.getPageNumber() - 1, 
                                pageRequest.getPageSize(), 
                                Sort.by(pageRequest.getSortBy()));

        List<FoodWithFavorite> foods = foodRepository
                    .findAllWithSpec(
                        foodRequestDto.getFoodName(), 
                        foodRequestDto.getCategoryId(), 
                        userId, pageable)
                    .toList();
                    
        if (foods.isEmpty()) {
            return ApiResponseWithData.builder()
                    .data(foods)
                    .total(0)
                    .message("Makanan tidak tersedia")
                    .status(HttpStatus.NO_CONTENT.toString())
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .build();
        }

        List<FoodDto> foodDtos = new ArrayList<>();
        foodDtos = foods.stream().map(food -> modelMapper.map(food, FoodDto.class)).toList();

        return ApiResponseWithData.builder()
                .data(foodDtos)
                .total(foodDtos.size())
                .message("OK")
                .status(status.toString())
                .statusCode(status.value())
                .build();
    }

    public ApiResponseWithData getFoodDetail(Integer foodId) {
        HttpStatus status = HttpStatus.OK;
        if (!foodRepository.existsById(foodId)) {
            return ApiResponseWithData.builder()
                    .data("")
                    .total(0)
                    .message("Detail makanan tidak tersedia")
                    .status(status.toString())
                    .statusCode(status.value())
                    .build();
        }
        Integer userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<FoodWithFavorite> foodOptional = foodRepository.findByIdWithFavorite(foodId, userId);
        FoodWithFavorite food = null;
        if (foodOptional.isPresent()) {
            food = foodOptional.get();
        }
        else {
            throw new DataNotFoundException("Data tidak ditemukan");
        }
        FoodDto foodDto = modelMapper.map(food, FoodDto.class);
        return ApiResponseWithData.builder()
                .data(foodDto)
                .total(1)
                .message("Berhasil memuat data detail makanan")
                .status(status.toString())
                .statusCode(status.value())
                .build();
    }

    public ApiResponseWithData getAllFavorite() {
        HttpStatus status = HttpStatus.OK;
        try {
            Integer userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<FoodWithFavorite> favoriteFoods = foodRepository.findAllFavorite(PageRequest.of(0, 8), userId).toList();
            List<FoodDto> foodDtos = new ArrayList<>();
            foodDtos = favoriteFoods.stream().map(food -> modelMapper.map(food, FoodDto.class)).toList();
            return ApiResponseWithData.builder()
                .data(foodDtos)
                .total(foodDtos.size())
                .message("Berhasil memuat data favorite makanan")
                .status(status.toString())
                .statusCode(status.value())
                .build();
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException("Data favorite tidak ditemukan atau tidak tersedia");
        } catch (Exception e) {
            throw e;
        }
    }

    public ApiResponseWithData toggleFavorites(Integer foodId) {
        FavoriteFood favoriteFood = null;
        String message = "";
        HttpStatus status = null;
        if(!foodRepository.existsById(foodId)) {
            return ApiResponseWithData.builder()
                        .data("")
                        .total(0)
                        .message("Makanan tidak ditemukan")
                        .status(HttpStatus.NOT_FOUND.toString())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build();
        }
        int userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userRepository.findById(userId).get();
        Food food = foodRepository.findById(foodId).get();
        if (favoriteFoodRepository.existsFavoriteFoodsByFoodIdAndUserId(food, user)) {
            favoriteFood = List.copyOf(favoriteFoodRepository.findFavoriteFoodByFoodIdAndUserId(food, user)).get(0);
            favoriteFood.setFavorite(!favoriteFood.isFavorite());
            favoriteFoodRepository.updateOrInsert(favoriteFood);
            status = HttpStatus.OK;
            if (favoriteFood.isFavorite()) {
                message = String.format("Makanan %s berhasil ditambakan ke dalam favorit", favoriteFood.getFood().getFoodName());
            }
            else {
                message = String.format("Makanan %s berhasil dihapus dari dalam favorit", favoriteFood.getFood().getFoodName());
            }
            
        }
        else {
            favoriteFood = FavoriteFood.builder()
                            .food(food)
                            .user(user)
                            .isFavorite(true)
                            .build();
            favoriteFoodRepository.save(favoriteFood);
            status = HttpStatus.CREATED;
            message = String.format("Makanan %s berhasil dihapus dari dalam favorit", favoriteFood.getFood().getFoodName());
        }
        return ApiResponseWithData.builder()
                .data("")
                .total(1)
                .message(String.format(message, favoriteFood.getFood().getFoodName()))
                .status(status.toString())
                .statusCode(status.value())
                .build();
    }

    public ApiResponseWithData getAllCarts() {
        return cartService.getAllCarts();
    }

    public ResponseEntity<Long> getCartTotal() {
        return cartService.getCartTotal();
    }

    public ApiResponseWithData addToCart(Integer foodId) {
        return cartService.addToCart(foodId);
    }

    public ApiResponseWithData setFoodQuantityInCart(Integer cartId, CartRequest request) {
        return cartService.setFoodQuantity(cartId, request);
    }

    public ApiResponseWithData deleteFoodFromCart(Integer cartId) {
        return cartService.deleteItem(cartId);
    }

    public ApiResponseWithData checkout(CartCheckoutRequest request) {
        return cartService.checkout(request);
    }

    public ApiResponseWithData orderHistory() {
        Integer userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);
        
        if (orders.isEmpty()) {
            throw new DataNotFoundException("History Pesanan Tidak Ditemukan atau Tidak Tersedia");
        }
        List<OrderHistoryDto> orderHistoryDtos = orders.stream().map(order -> modelMapper.map(order, OrderHistoryDto.class)).toList();
        for (OrderHistoryDto order: orderHistoryDtos) {
            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(order.getOrderId());
            List<OrderDetailDto> orderDetailDtos = orderDetails.stream().map(orderDetail -> modelMapper.map(orderDetail, OrderDetailDto.class)).toList();
            order.setOrderDetails(orderDetailDtos);
        }

        return ApiResponseWithData.builder()
                .data(orderHistoryDtos)
                .total(orderHistoryDtos.size())
                .message("Berhasil memuat history belanja")
                .status(HttpStatus.OK.toString())
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public String test(String authorization) {
        return "";
    }

    public ResponseEntity<ApiResponseWithData> foodAlsoWithCart(Integer userId) {
        List<FoodWithFavorite> foods = foodRepository.findAllCustomViewByUserId(PageRequest.of(0, 100), userId).toList();
        if (foods.size() == 0 || foods == null) {
            log.debug("Data tidak ditemukan");
            throw new DataNotFoundException("Data tidak ditemukan");
        }
        log.debug("Total data: " + foods.size());
        return ResponseEntity.ok().body(ApiResponseWithData.builder().total(foods.size()).data(foods).message("ok").status("200 OK").statusCode(200).build());
    }
}
