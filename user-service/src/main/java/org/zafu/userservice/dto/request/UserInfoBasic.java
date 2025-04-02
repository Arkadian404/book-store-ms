package org.zafu.userservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoBasic {
    private String username;
    private String role;
}
