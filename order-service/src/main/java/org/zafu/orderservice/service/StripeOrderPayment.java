package org.zafu.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zafu.orderservice.client.BookClient;
import org.zafu.orderservice.client.PaymentClient;
import org.zafu.orderservice.dto.request.PaymentMethod;
import org.zafu.orderservice.dto.request.StripeItemRequest;
import org.zafu.orderservice.dto.request.StripeRequest;
import org.zafu.orderservice.dto.response.OrderResponse;
import org.zafu.orderservice.dto.response.StripeResponse;
import org.zafu.orderservice.exception.AppException;
import org.zafu.orderservice.exception.ErrorCode;
import org.zafu.orderservice.mapper.OrderItemMapper;
import org.zafu.orderservice.mapper.OrderMapper;
import org.zafu.orderservice.model.Order;
import org.zafu.orderservice.model.OrderItem;
import org.zafu.orderservice.model.OrderStatus;
import org.zafu.orderservice.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "STRIPE-ORDER")
public class StripeOrderPayment implements OrderPayment {
    private final PaymentClient paymentClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final BookClient bookClient;

    @Override
    public PaymentMethod supports() {
        return PaymentMethod.STRIPE;
    }

    @Override
    public OrderResponse process(Order order, List<OrderItem> orderItems) {
        List<StripeItemRequest> stripeItems = orderItems.stream()
                .map(i -> orderItemMapper.toPaymentItemRequest(i, bookClient))
                .toList();

        StripeResponse stripe = paymentClient.createPaymentSession(StripeRequest.builder()
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .userId(order.getUserId())
                        .items(stripeItems)
                        .currency("VND")
                        .build())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_FAILED))
                .getResult();

        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);

        OrderResponse response = orderMapper.toOrderResponse(order, bookClient);
        response.setPaymentUrl(stripe.getSessionUrl());
        return response;
    }
}
