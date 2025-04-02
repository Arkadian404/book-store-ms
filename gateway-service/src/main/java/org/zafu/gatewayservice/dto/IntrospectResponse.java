package org.zafu.gatewayservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectResponse {
    boolean valid;
}
