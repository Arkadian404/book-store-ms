package org.zafu.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zafu.paymentservice.dto.ApiResponse;
import org.zafu.paymentservice.dto.request.UpdateOrderStatusRequest;
import org.zafu.paymentservice.dto.response.OrderResponse;

import java.util.Optional;

@FeignClient(
        name = "order-service",
        url = "${app.client.order-url}"
)
public interface OrderClient {
    @GetMapping("/orders/code/{orderCode}")
    Optional<ApiResponse<OrderResponse>> getOrderByOrderCode(@PathVariable String orderCode);
    @PutMapping("/orders/code/{orderCode}")
    Optional<ApiResponse<Void>> updateOrderStatus(@PathVariable String orderCode, @RequestBody UpdateOrderStatusRequest request);
}
