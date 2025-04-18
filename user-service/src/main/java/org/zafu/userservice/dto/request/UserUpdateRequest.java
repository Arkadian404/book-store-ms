package org.zafu.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    @NotNull(message = "Firstname is required")
    @NotBlank(message = "Firstname can't be blank")
    private String firstname;
    @NotNull(message = "Lastname is required")
    @NotBlank(message = "Lastname can't be blank")
    private String lastname;
    @NotNull(message = "Phone is required")
    @NotBlank(message = "Phone can't be blank")
    private String phone;
    @NotNull(message = "Address is required")
    @NotBlank(message = "Address can't be blank")
    private String address;
    @NotNull(message = "Province is required")
    @NotBlank(message = "Province can't be blank")
    private String province;
    @NotNull(message = "District is required")
    @NotBlank(message = "District can't be blank")
    private String district;
    @NotNull(message = "Ward is required")
    @NotBlank(message = "Ward can't be blank")
    private String ward;
}
