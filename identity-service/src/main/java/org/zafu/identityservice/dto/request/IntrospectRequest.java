package org.zafu.identityservice.dto.request;

import lombok.Getter;

@Getter
public class IntrospectRequest {
    private String token;
}
