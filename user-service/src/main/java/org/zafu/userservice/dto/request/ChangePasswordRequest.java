package org.zafu.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    @NotNull(message ="Email is required")
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;
}
