package org.zafu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password can't be blank")
    private String password;
    @NotNull(message = "Firstname is required")
    @NotBlank(message = "Firstname can't be blank")
    private String firstname;
    @NotNull(message = "Lastname is required")
    @NotBlank(message = "Lastname can't be blank")
    private String lastname;
    @NotNull(message = "Phone is required")
    @NotBlank(message = "Phone can't be blank")
    private String phone;
}
