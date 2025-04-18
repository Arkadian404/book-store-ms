package org.zafu.identityservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicUserInfo {
    private Integer userId;
    private String username;
    private String role;
}
