package com.padepokan79.foodorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse{

    private Integer userId;
    private String token;
    private String type;
    private String username;
    private String role;
    
}
