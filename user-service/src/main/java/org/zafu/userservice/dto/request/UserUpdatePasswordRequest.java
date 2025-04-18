package org.zafu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserUpdatePasswordRequest {
    @NotNull(message = "Old password is required")
    @NotBlank(message = "Old password can't be blank")
    private String oldPassword;
    @NotNull(message = "New password is required")
    @NotBlank(message = "New password can't be blank")
    private String newPassword;
    @NotNull(message = "Confirm password is required")
    @NotBlank(message = "Confirm password can't be blank")
    private String confirmPassword;
}
