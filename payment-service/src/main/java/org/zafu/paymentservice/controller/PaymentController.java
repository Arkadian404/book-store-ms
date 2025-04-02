package org.zafu.paymentservice.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zafu.paymentservice.dto.ApiResponse;
import org.zafu.paymentservice.dto.request.PaymentRequest;
import org.zafu.paymentservice.dto.request.StripeRequest;
import org.zafu.paymentservice.dto.response.StripeResponse;
import org.zafu.paymentservice.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Slf4j(topic = "PAYMENT-CONTROLLER")
public class PaymentController {
    private final PaymentService paymentService;
    @Value("${app.stripe.webhook-signing-key}")
    private String signingKey;

    @PostMapping
    public ApiResponse<String> savePayment(
            @RequestBody PaymentRequest request
            ){
        paymentService.savePayment(request);
        return ApiResponse.<String>builder()
                .message("Payment saved")
                .result("Payment saved")
                .build();
    }


    @PostMapping("/checkout/stripe")
    public ApiResponse<StripeResponse> stripeCheckout(
            @RequestBody StripeRequest request
            ){
        return ApiResponse.<StripeResponse>builder()
                .message("Payment successful")
                .result(paymentService.stripeCheckout(request))
                .build();
    }


    @PostMapping("/webhooks/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ){
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, signingKey);
        } catch (SignatureVerificationException e) {
            log.error("Invalid Stripe signature: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if(deserializer.getObject().isPresent()){
            stripeObject = deserializer.getObject().get();
            log.info("Stripe object: {}", stripeObject);
        }else {
            log.error("Deserialization failed for Event: {}", event);
            throw new RuntimeException("Deserialization failed for Event");
        }
        log.info("Received event: {}", event.getId());
        String type = event.getType();
        if (type.equals("checkout.session.completed")) {
            log.info("Checkout session completed");
            Session session = (Session) stripeObject;
            String orderCode = session.getMetadata().get("orderCode");
            paymentService.handleStripePaymentSuccess(orderCode, session.getId());
        } else if (type.equals("checkout.session.expired")) {
            log.info("Checkout session expired");
        }
        return ResponseEntity.ok().build();
    }

}

