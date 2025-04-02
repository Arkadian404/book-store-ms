package org.zafu.identityservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.zafu.identityservice.dto.ApiResponse;
import org.zafu.identityservice.dto.response.UserInfo;

import java.util.Optional;

@FeignClient(
        name = "user-service",
        url = "${app.client.user-url}"
)
public interface UserClient {
    @GetMapping("/username/{username}")
    Optional<ApiResponse<UserInfo>> findUserClientByUsername(@PathVariable String username);
}
