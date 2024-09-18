package com.padepokan79.foodorder.service.usermanagement;

import java.net.URI;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.padepokan79.foodorder.dto.request.LoginRequest;
import com.padepokan79.foodorder.dto.request.RegisterRequest;
import com.padepokan79.foodorder.dto.response.LoginResponse;
import com.padepokan79.foodorder.dto.response.MessageReponseWithData;
import com.padepokan79.foodorder.dto.response.MessageResponse;
import com.padepokan79.foodorder.exception.classes.ValidationException;
import com.padepokan79.foodorder.model.User;
import com.padepokan79.foodorder.repository.UserRepository;
import com.padepokan79.foodorder.security.jwt.JwtUtils;
import com.padepokan79.foodorder.security.service.UserDetailsImplement;
import com.padepokan79.foodorder.utils.MessageUtils;
import com.padepokan79.foodorder.utils.ViolationsMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    
    @Autowired
    private Validator validator;
    @Autowired
    private MessageUtils messageUtils;

    public UserService(
        final UserRepository userRepository, 
        final PasswordEncoder passwordEncoder, final AuthenticationManager authenticationManager, 
        final JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public ResponseEntity<MessageResponse> registerUser(RegisterRequest request) {
        
        HttpStatus status = HttpStatus.CREATED;
        Set<ConstraintViolation<RegisterRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ValidationException(ViolationsMapper.map(constraintViolations));
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            String message = messageUtils.getMessage("register.username.exists");
            throw new ValidationException(ViolationsMapper.map("username", message));
        }
        if (!request.getPassword().equals(request.getRetypePassword())) {
            String message = messageUtils.getMessage("register.retypepassword.notmatch");
            throw new ValidationException(ViolationsMapper.map("retypePassword", message));
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                    .username(request.getUsername())
                    .fullname(request.getFullname())
                    .password(encodedPassword)
                    .build();
        userRepository.save(user);
        String message = String.format(messageUtils.getMessage("register.success"), user.getUsername());
        log.info(message);
        URI location = 
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getUserId())
                .toUri();
        return ResponseEntity.created(location)
                .body(new MessageResponse(message, status.toString(), status.value()));
    }

    public ResponseEntity<MessageReponseWithData> userLogin(LoginRequest request) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(request);
        log.info("username : " + request.getUsername());
        log.info("password : " + request.getPassword());
        
        if (!constraintViolations.isEmpty()) {
            throw new ValidationException(ViolationsMapper.map(constraintViolations));
        }
        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException(ViolationsMapper.map("username", messageUtils.getMessage("username.error.notfound")));
        }
        try {
            Authentication authentication = 
                            authenticationManager
                            .authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImplement userDetails = (UserDetailsImplement) authentication.getPrincipal();
            // Optional<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).findFirst();
            LoginResponse loginResponse = LoginResponse.builder()
                                    .userId(userDetails.getId())
                                    .username(userDetails.getUsername())
                                    .token(jwt)
                                    .type("Bearer")
                                    .role("USER")
                                    .build();
            message = messageUtils.getMessage("login.success");
            log.info(message);
            return ResponseEntity.ok().body(
                    MessageReponseWithData.builder()
                    .total(1)
                    .data(loginResponse)
                    .message(message)
                    .status(status.toString())
                    .statusCode(status.value())
                    .build()
            );
        } catch (AuthenticationException e) {
            throw new ValidationException(ViolationsMapper.map("password", messageUtils.getMessage("password.error.notmatch")));
        }
    }

    public ResponseEntity<String> testAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok().body(authentication.getName());
    }
    
}
