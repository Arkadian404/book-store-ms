package org.zafu.userservice.client;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.zafu.userservice.dto.ApiResponse;
import org.zafu.userservice.dto.request.UserInfoBasic;

@FeignClient(
        name = "IDENTITY-SERVICE",
        url = "${app.client.identity-url}"
)
public interface IdentityClient {
    @PostMapping("/tokens/generate-verification-token")
    ApiResponse<String> generateVerificationToken(@RequestBody UserInfoBasic request);

    @GetMapping("/tokens/verify-token")
    ApiResponse<String> verifyToken(@RequestParam String token);
}
