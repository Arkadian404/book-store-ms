package org.zafu.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String phone;
    private String role;
}
