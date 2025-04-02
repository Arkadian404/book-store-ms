package org.zafu.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username can't be blank")
    private String username;
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password can't be blank")
    private String password;
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email is invalid")
    private String email;
}
