package org.zafu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class ResetPasswordRequest {
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotNull(message = "Confirm password cannot be null")
    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
}
