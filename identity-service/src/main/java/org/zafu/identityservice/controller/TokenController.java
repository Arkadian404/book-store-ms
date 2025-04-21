package org.zafu.identityservice.controller;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zafu.identityservice.dto.ApiResponse;
import org.zafu.identityservice.dto.response.BasicUserInfo;
import org.zafu.identityservice.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
public class TokenController {
    private final AuthenticationService service;

    @PostMapping("/generate-verification-token")
    ApiResponse<String> generateVerificationToken(@RequestBody BasicUserInfo userInfo){
        return ApiResponse.<String>builder()
                .message("Create token successfully")
                .result(service.genEmailToken(userInfo))
                .build();
    }

    @GetMapping("/verify-token")
    ApiResponse<String> verifyToken(@RequestParam String token){
        SignedJWT jwt = service.verify(token);
        return ApiResponse.<String>builder()
                .message("Verify token!")
                .result(jwt.getParsedString())
                .build();
    }
}
