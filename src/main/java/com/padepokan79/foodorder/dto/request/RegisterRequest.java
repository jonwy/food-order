package com.padepokan79.foodorder.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotEmpty (message = "{username.required}")
    @Size(min = 2, message = "{username.length}")
    private String username;

    @NotEmpty(message = "{fullname.required}")
    private String fullname;

    @NotEmpty(message = "{password.required}")
    @Size(min = 6, message = "{password.length}")
    private String password;

    @NotEmpty(message =  "{password.retype.required}")
    @Size(min = 6, message = "{password.length}")
    private String retypePassword;
}
