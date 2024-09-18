package com.padepokan79.foodorder.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "{username.required}")
    private String username;
    @NotEmpty(message = "{password.required}")
    private String password;
}
