package com.padepokan79.foodorder.service.foodorder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.padepokan79.foodorder.dto.CartDto;
import com.padepokan79.foodorder.dto.request.CartCheckoutRequest;
import com.padepokan79.foodorder.dto.request.CartRequest;
import com.padepokan79.foodorder.dto.response.MessageResponse;
import com.padepokan79.foodorder.exception.classes.CartException;
import com.padepokan79.foodorder.exception.classes.DataNotFoundException;
import com.padepokan79.foodorder.model.Cart;
import com.padepokan79.foodorder.model.Food;
import com.padepokan79.foodorder.model.Order;
import com.padepokan79.foodorder.model.OrderDetail;
import com.padepokan79.foodorder.model.User;
import com.padepokan79.foodorder.repository.CartRepository;
import com.padepokan79.foodorder.repository.FoodRepository;
import com.padepokan79.foodorder.repository.OrderDetailRepository;
import com.padepokan79.foodorder.repository.OrderRepository;
import com.padepokan79.foodorder.repository.UserRepository;
import com.padepokan79.foodorder.security.service.UserDetailsImplement;
import com.padepokan79.foodorder.utils.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
    
    private final CartRepository cartRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    public ResponseEntity<MessageResponse> getAllCarts() {
        HttpStatus status = HttpStatus.OK;
        Integer userId = ((UserDetailsImplement) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userRepository.findById(userId).get();
        List<Cart> carts = cartRepository.findNotDeletedCartByUserId(user);
        List<CartDto> cartDtos = carts.stream().map(item -> modelMapper.map(item, CartDto.class)).toList();
        return ResponseUtil.createResponse(status, "Data keranjang berhasil dimuat.", cartDtos);
    }

    public ResponseEntity<Long> getCartTotal() {
        int userId = ((UserDetailsImplement) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Long total = cartRepository.countByUserId(userId);
        return ResponseEntity.ok().body(total);
    }

    @Transactional
    public ResponseEntity<MessageResponse> addToCart(Integer foodId) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        if (!foodRepository.existsById(foodId)) {
            message = "Makanan tidak tersedia";
            log.info("Makanan tidak tersedia atau ditemukan");
            throw new DataNotFoundException(message);
        }
        Food food = foodRepository.findById(foodId).get();
        int userId = ((UserDetailsImplement) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userRepository.findById(userId).get();
        Cart cart = null;
        if (!cartRepository.existsByFoodIdAndUserId(food, user)) {
            cart = Cart.builder()
            .food(food)
            .user(user)
            .quantity(1)
            .isDeleted(false)
            .build();
            cartRepository.save(cart);
            message = String.format("Makanan %s berhasil ditambahkan ke dalam keranjang", cart.getFood().getFoodName());
            log.info(message);
        }
        else {
            cart = cartRepository.findByFoodIdAndUserId(food, user).orElseThrow(() -> new DataNotFoundException("Makanan tidak ditemukan di cart"));
            cart.setDeleted(!cart.isDeleted());
            if (cart.isDeleted()) {
                cart.setQuantity(0);
                message = String.format("Makanan %s berhasil dihapus dari keranjang", cart.getFood().getFoodName());
            }
            else {
                cart.setQuantity(1);
                message = String.format("Makanan %s berhasil ditambahkan ke dalam keranjang", cart.getFood().getFoodName());
            }
        }
        return ResponseUtil.createResponse(status, message);
    }

    @Transactional
    public ResponseEntity<MessageResponse> setFoodQuantity(Integer cartId, CartRequest request) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new DataNotFoundException("Makanan tidak ditemukan di keranjang"));
        
        int currQuantity = cart.getQuantity();
        log.info(cart.getFood().getFoodName() + ", foodId: " + cart.getFood().getFoodId() + ", qty: " + cart.getQuantity());
        if (currQuantity > 0) {
            log.info("update quantity");
            cart.setQuantity(currQuantity + request.getQuantity());
            message = "berhasil update quantity";
        }
        else if (currQuantity + request.getQuantity() < 1) {
            log.info("quantity = 1");
            cart.setQuantity(0);
            cart.setDeleted(true);
            message = "berhasil menghapus item";
        }
        
        cartRepository.save(cart);
        return ResponseUtil.createResponse(status, message);
    }

    @Transactional
    public ResponseEntity<MessageResponse> deleteItem(Integer cartId) {
        HttpStatus status = HttpStatus.OK;
        // Integer userId = ((UserDetailsImplement) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new DataNotFoundException("Makanan tidak ditemukan di keranjang"));
        cart.setDeleted(true);
        cartRepository.save(cart);
        return ResponseUtil.createResponse(status, "Berhasil menghapus item dari keranjang");
    }

    @Transactional
    public ResponseEntity<MessageResponse> checkout(CartCheckoutRequest request) {
        if (request.getCartIds() == null || request.getCartIds().isEmpty()) {
            throw new CartException("Keranjang tidak boleh kosong");
        }
        Integer userId = ((UserDetailsImplement) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userRepository.findById(userId).orElseThrow();
        List<Cart> carts = cartRepository.findNotDeletedCartByUserId(user);
        List<Cart> cartsToCheckout = new ArrayList<>();
        if (carts.isEmpty()) {
            throw new DataNotFoundException("Keranjang kosong atau tidak tersedia");
        }
        cartsToCheckout = carts.stream().filter(item -> request.getCartIds().contains(item.getCartId())).toList();
        Integer totalItem = cartsToCheckout.stream().mapToInt(item -> item.getQuantity()).sum();;
        Integer totalPrice = cartsToCheckout.stream().mapToInt(item -> item.getQuantity() * item.getFood().getPrice()).sum();
        Order order = Order.builder()
                    .orderDate(LocalDate.now())
                    .totalItem(totalItem)
                    .totalOrderPrice(totalPrice)
                    .user(user)
                    .build();

        List<OrderDetail> orderDetails = new ArrayList<>();
        
        cartsToCheckout.forEach(item -> {
            OrderDetail orderDetail = OrderDetail.builder()
                                .food(item.getFood())
                                .order(order)
                                .totalPrice(item.getQuantity() * item.getFood().getPrice())
                                .quantity(item.getQuantity())
                                .build();
            orderDetails.add(orderDetail);
            item.setDeleted(true);
        });

        orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetails);
        cartRepository.saveAll(carts);
        return ResponseUtil.createResponse(HttpStatus.OK, "Keranjang berhasil dicheckout.");
    }


}
