package org.zafu.gatewayservice.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import org.zafu.gatewayservice.dto.ApiResponse;
import org.zafu.gatewayservice.dto.IntrospectRequest;
import org.zafu.gatewayservice.dto.IntrospectResponse;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}
