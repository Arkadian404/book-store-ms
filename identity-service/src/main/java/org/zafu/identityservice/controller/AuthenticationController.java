package org.zafu.identityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zafu.identityservice.dto.*;
import org.zafu.identityservice.dto.request.AuthenticationRequest;
import org.zafu.identityservice.dto.request.IntrospectRequest;
import org.zafu.identityservice.dto.request.LogoutRequest;
import org.zafu.identityservice.dto.request.RefreshRequest;
import org.zafu.identityservice.dto.response.AuthenticationResponse;
import org.zafu.identityservice.dto.response.IntrospectResponse;
import org.zafu.identityservice.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
            ){
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login successfully")
                .result(service.login(request))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(
            @RequestBody @Valid RefreshRequest request
    ){
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Refresh token successfully")
                .result(service.refreshToken(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestBody LogoutRequest request
    ){
        service.logout(request);
        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspectToken(
            @RequestBody IntrospectRequest request
    ){
        return ApiResponse.<IntrospectResponse>builder()
                .message("Introspect token successfully")
                .result(service.introspect(request))
                .build();
    }

}
