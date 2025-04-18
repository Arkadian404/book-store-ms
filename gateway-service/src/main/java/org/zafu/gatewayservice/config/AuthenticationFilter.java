package org.zafu.gatewayservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.zafu.gatewayservice.client.IdentityClient;
import org.zafu.gatewayservice.dto.ApiResponse;
import org.zafu.gatewayservice.dto.ErrorResponse;
import org.zafu.gatewayservice.dto.IntrospectRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-FILTER")
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final ObjectMapper objectMapper;
    private final IdentityClient client;
    @Value("${app.api-prefix}")
    private String apiPrefix;
    private static final String[] PUBLIC_ENDPOINTS = {
            "/v3/api-docs/.*",
            "/swagger-config.json",
            "/auth/.*",
            "/tokens/.*",
            "/users",
            "/users/location/.*",
            "/users/secured",
            "/users/register",
            "/users/verify",
            "/users/password/.*",
            "/books",
            "/books/slug/.*",
            "/books/sides/.*",
            "/books/authors",
            "/books/authors/category/.*",
            "/books/publishers",
            "/books/publishers/category/.*",
            "/books/categories",
            "/books/google-books/.*",
            "/books/page/.*",
            "/books/search",
            "/books/search/[0-9]+",
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("GO TO GATEWAY FILTER FIRST");
        log.info("Exchange request: {}", exchange.getRequest().getPath());
        if(isPublicEndpoints(exchange.getRequest())){
            return chain.filter(exchange);
        }
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(Collections.isEmpty(authHeader)){
            return unauthenticated(exchange.getResponse());
        }
        String authToken = authHeader.get(0).replace("Bearer ", "");
        log.info("Token: {}", authToken);
        IntrospectRequest request = IntrospectRequest.builder().token(authToken).build();
        return client.introspect(request).flatMap(response ->{
            if(response.getResult().isValid()){
                return chain.filter(exchange);
            }else{
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthenticated")
                .timestamp(LocalDateTime.now())
                .path("/error")
                .build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(errorResponse);
        }catch (JsonProcessingException ex){
            log.info("JSON Process failed {}", ex.getMessage());
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(Objects.requireNonNull(body).getBytes())));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    boolean isPublicEndpoints(ServerHttpRequest request){
        return Arrays.stream(PUBLIC_ENDPOINTS)
                .anyMatch(s->request.getURI().getPath().matches(apiPrefix+s));
    }
}
