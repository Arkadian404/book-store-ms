package org.zafu.paymentservice.service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zafu.paymentservice.client.BookClient;
import org.zafu.paymentservice.client.CartClient;
import org.zafu.paymentservice.dto.request.*;
import org.zafu.paymentservice.dto.response.OrderItemResponse;
import org.zafu.paymentservice.mapper.PaymentMapper;
import org.zafu.paymentservice.repository.PaymentRepository;
import org.zafu.paymentservice.client.OrderClient;
import org.zafu.paymentservice.dto.response.OrderResponse;
import org.zafu.paymentservice.dto.response.OrderStatus;
import org.zafu.paymentservice.dto.response.StripeResponse;
import org.zafu.paymentservice.exception.AppException;
import org.zafu.paymentservice.exception.ErrorCode;
import org.zafu.paymentservice.model.Payment;
import org.zafu.paymentservice.model.PaymentMethod;
import org.zafu.paymentservice.model.PaymentStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PAYMENT-SERVICE")
public class PaymentService {
    @Value("${app.stripe.secret-key}")
    private String secretKey;

    private final BookClient bookClient;
    private final OrderClient orderClient;
    private final CartClient cartClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProducer paymentProducer;

    @Transactional
    public void savePayment(PaymentRequest request){
        Payment payment = paymentMapper.toPayment(request);
        paymentRepository.save(payment);
    }


    public StripeResponse stripeCheckout(StripeRequest request){
        Stripe.apiKey = secretKey;

        List<SessionCreateParams.LineItem> lineItems = request.getItems().stream()
                .map(item -> SessionCreateParams.LineItem.builder()
                        .setQuantity((long) item.getBookQuantity())
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD")
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getBookTitle())
                                                        .addImage(item.getBookImageUrl())
                                                        .build()
                                        )
                                        .setUnitAmount((long) item.getBookPrice())
                                        .build()
                        )
                        .build())
                .toList();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addAllLineItem(lineItems)
                .putMetadata("orderCode", request.getOrderCode())
                .setSuccessUrl("http://localhost:4200/order/success?orderCode="+request.getOrderCode())
                .setCancelUrl("http://localhost:8050/views/cancel")
                .build();
        try {
            Session session = Session.create(params);
            return StripeResponse.builder()
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .status("Success payment")
                    .message("Session created successfully")
                    .build();
        }catch (StripeException e) {
            log.error("Error ", e);
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        } catch (Exception e) {
            log.error("Error ", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


    @Transactional
    public void handleStripePaymentSuccess(String orderCode, String sessionId){
        OrderResponse orderResponse = orderClient.getOrderByOrderCode(orderCode)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND))
                .getResult();
        UpdateOrderStatusRequest request =  UpdateOrderStatusRequest.builder()
                .status(OrderStatus.PAID)
                .build();
        orderClient.updateOrderStatus(orderResponse.getOrderCode(), request);
        for (OrderItemResponse orderItem : orderResponse.getItems()) {
            UpdateStockRequest updateStockRequest = new UpdateStockRequest();
            updateStockRequest.setQuantity(orderItem.getBookQuantity());
            bookClient.updateStock(orderItem.getBookId(), updateStockRequest);
        }
        cartClient.clearCart(orderResponse.getUserId());
        Payment payment = Payment.builder()
                .orderId(orderResponse.getId())
                .userId(orderResponse.getUserId())
                .totalAmount(orderResponse.getTotalAmount())
                .type(PaymentMethod.STRIPE)
                .status(PaymentStatus.SUCCESS)
                .sessionId(sessionId)
                .build();
        paymentRepository.save(payment);
        OrderConfirmation orderConfirmation = paymentMapper.toOrderConfirmation(orderResponse);
        paymentProducer.sendPaymentConfirmation(orderConfirmation);
    }

}
