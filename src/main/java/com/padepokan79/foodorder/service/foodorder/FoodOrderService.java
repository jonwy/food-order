package com.padepokan79.foodorder.service.foodorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.padepokan79.foodorder.dto.FoodDto;
import com.padepokan79.foodorder.dto.FoodProjection;
import com.padepokan79.foodorder.dto.OrderDetailDto;
import com.padepokan79.foodorder.dto.OrderHistoryDto;
import com.padepokan79.foodorder.dto.request.CartCheckoutRequest;
import com.padepokan79.foodorder.dto.request.CartRequest;
import com.padepokan79.foodorder.dto.request.FoodRequestDto;
import com.padepokan79.foodorder.dto.request.PageRequestDto;
import com.padepokan79.foodorder.dto.response.MessageResponse;
import com.padepokan79.foodorder.dto.response.MessageResponse.Meta;
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
import com.padepokan79.foodorder.utils.ResponseUtil;

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

    public ResponseEntity<MessageResponse> getAllFoods(PageRequestDto pageRequest, FoodRequestDto foodRequestDto) {
        try {
            String message = "Data makanan berhasil dimuat";
            int userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            
            Pageable pageable = PageRequest.of(
                                    pageRequest.getPageNumber() - 1, 
                                    pageRequest.getPageSize(), 
                                    Sort.by(pageRequest.getSortBy()));

            
            Page<FoodProjection> foodPages = foodRepository
                                        .findAll(
                                            "%" + foodRequestDto.getFoodName() + "%",
                                            foodRequestDto.getCategoryId(),
                                            userId, pageable);
            List<FoodProjection> foods = foodPages.toList();
            Meta meta = new Meta(foodPages.getTotalPages(), pageRequest.getPageSize(), pageRequest.getPageNumber());
            return ResponseUtil.createResponse(HttpStatus.OK, message, foods, meta);
        } catch (Exception ex) {
            return ResponseUtil.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        
    }

    // TODO : refactor
    public ResponseEntity<MessageResponse> getFoodDetail(Integer foodId) {
        try {
            if (!foodRepository.existsById(foodId)) {
                throw new DataNotFoundException("Data detail makanan tidak ditemukan/tersedia");
            }
            Integer userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            Optional<FoodProjection> foodOptional = foodRepository.findById(foodId, userId);
            FoodProjection food = null;
            if (foodOptional.isPresent()) {
                food = foodOptional.get();
            }
            else {
                throw new DataNotFoundException("Data tidak ditemukan");
            }
            FoodDto foodDto = modelMapper.map(food, FoodDto.class);
            return ResponseUtil.createResponse(HttpStatus.OK, "Data makanan berhasil dimuat.", foodDto);
        } 
        catch (DataNotFoundException ex) {
           return ResponseUtil.createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (Exception ex) {
            return ResponseUtil.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        
    }

    public ResponseEntity<MessageResponse> getAllFavorite() {
        try {
            Integer userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<FoodProjection> favoriteFoods = foodRepository.findAllFavorite(PageRequest.of(0, 8), userId).toList();
            List<FoodDto> foodDtos = new ArrayList<>();
            foodDtos = favoriteFoods.stream().map(food -> modelMapper.map(food, FoodDto.class)).toList();
            return ResponseUtil.createResponse(HttpStatus.OK, "Data makanan berhasil dimuat.", foodDtos);
        } catch (DataNotFoundException e) {
            return ResponseUtil.createResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ResponseUtil.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<MessageResponse> toggleFavorites(Integer foodId) {
        try {
            FavoriteFood favoriteFood = null;
            String message = "";
            HttpStatus status = null;
            if(!foodRepository.existsById(foodId)) {
                throw new DataNotFoundException("Data makanan tidak ditemukan.");
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
            return ResponseUtil.createResponse(status, message);
        } 
        catch (DataNotFoundException e) {
            return ResponseUtil.createResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch (Exception e) {
            return ResponseUtil.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<MessageResponse> getAllCarts() {
        return cartService.getAllCarts();
    }

    public ResponseEntity<Long> getCartTotal() {
        return cartService.getCartTotal();
    }

    public ResponseEntity<MessageResponse> addToCart(Integer foodId) {
        return cartService.addToCart(foodId);
    }

    public ResponseEntity<MessageResponse> setFoodQuantityInCart(Integer cartId, CartRequest request) {
        return cartService.setFoodQuantity(cartId, request);
    }

    public ResponseEntity<MessageResponse> deleteFoodFromCart(Integer cartId) {
        return cartService.deleteItem(cartId);
    }

    public ResponseEntity<MessageResponse> checkout(CartCheckoutRequest request) {
        return cartService.checkout(request);
    }

    public ResponseEntity<MessageResponse> orderHistory() {
        try {
            Integer userId = ((UserDetailsImplement)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<Order> orders = orderRepository.findAllByUserId(userId);
            
            List<OrderHistoryDto> orderHistoryDtos = orders.stream().map(order -> modelMapper.map(order, OrderHistoryDto.class)).toList();
            for (OrderHistoryDto order: orderHistoryDtos) {
                List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(order.getOrderId());
                List<OrderDetailDto> orderDetailDtos = orderDetails.stream().map(orderDetail -> modelMapper.map(orderDetail, OrderDetailDto.class)).toList();
                order.setOrderDetails(orderDetailDtos);
            }
            return ResponseUtil.createResponse(HttpStatus.OK, "Berhasil memuat daftar riwayat belanja.", orderHistoryDtos);
        } catch (Exception e) {
            return ResponseUtil.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
