package com.padepokan79.foodorder.controller.usermanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.padepokan79.foodorder.dto.request.LoginRequest;
import com.padepokan79.foodorder.dto.request.RegisterRequest;
import com.padepokan79.foodorder.dto.response.MessageReponseWithData;
import com.padepokan79.foodorder.service.usermanagement.UserService;

@RestController
@RequestMapping("/user-management")
public class UserController {

    private final UserService userService;

   
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<MessageReponseWithData> login(@RequestBody LoginRequest request) {       
        return userService.userLogin(request);
        // throw new Exception("Error");
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<? extends Object> register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/test")
    public String testAuth() {
        System.out.println("Accessing test endpoint");
        return "Test successful";
    }
    
}
