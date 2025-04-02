package org.zafu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuthenticationRequest {
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username can't be blank")
    private String username;
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password can't be blank")
    private String password;
}
