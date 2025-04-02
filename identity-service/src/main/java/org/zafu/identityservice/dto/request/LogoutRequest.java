package org.zafu.identityservice.dto.request;

import lombok.Getter;

@Getter
public class LogoutRequest {
    private String token;
}
