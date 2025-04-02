package org.zafu.orderservice.client;

import org.mapstruct.Mapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.zafu.orderservice.dto.ApiResponse;
import org.zafu.orderservice.dto.response.CartResponse;

import java.util.Optional;

@FeignClient(
        name = "cart-service",
        url ="${app.client.cart-url}"
)
public interface CartClient {
    @GetMapping("/carts/user/{userId}")
    Optional<ApiResponse<CartResponse>> getCartByUserId(@PathVariable Integer userId);
    @DeleteMapping("/carts/user/{userId}")
    void clearCart(@PathVariable Integer userId);
}
