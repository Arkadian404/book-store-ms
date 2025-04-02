package org.zafu.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "cart-service",
        url = "${app.client.cart-url}"
)
public interface CartClient {
    @DeleteMapping("/carts/user/{userId}")
    void clearCart(@PathVariable Integer userId);
}
