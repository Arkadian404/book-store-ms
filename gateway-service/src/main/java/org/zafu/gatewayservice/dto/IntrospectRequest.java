package org.zafu.gatewayservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IntrospectRequest {
    private String token;
}
