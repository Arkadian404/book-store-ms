package org.zafu.userservice.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String role;
}
