package org.zafu.identityservice.dto.request;

import lombok.Getter;

@Getter
public class RefreshRequest {
    private String refreshToken;
}
