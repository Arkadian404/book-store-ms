package org.zafu.userservice.dto.request;

import lombok.Getter;


@Getter
public class ResetPasswordRequest {
    private String password;
    private String confirmPassword;
}
