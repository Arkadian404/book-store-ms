package org.zafu.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zafu.orderservice.dto.ApiResponse;
import org.zafu.orderservice.dto.request.PaymentRequest;
import org.zafu.orderservice.dto.request.StripeRequest;
import org.zafu.orderservice.dto.response.StripeResponse;

import java.util.Optional;

@FeignClient(
        name = "payment-service",
        url = "${app.client.payment-url}"
)
public interface PaymentClient {
    @PostMapping("/payments/checkout/stripe")
    Optional<ApiResponse<StripeResponse>> createPaymentSession(@RequestBody StripeRequest request);
    @PostMapping("/payments")
    Optional<ApiResponse<String>> savePayment(@RequestBody PaymentRequest request);
}
