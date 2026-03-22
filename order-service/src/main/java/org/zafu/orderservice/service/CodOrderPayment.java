package org.zafu.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zafu.orderservice.client.BookClient;
import org.zafu.orderservice.client.CartClient;
import org.zafu.orderservice.client.PaymentClient;
import org.zafu.orderservice.dto.request.PaymentMethod;
import org.zafu.orderservice.dto.request.PaymentRequest;
import org.zafu.orderservice.dto.request.PaymentStatus;
import org.zafu.orderservice.dto.request.UpdateStockRequest;
import org.zafu.orderservice.dto.response.OrderResponse;
import org.zafu.orderservice.exception.AppException;
import org.zafu.orderservice.exception.ErrorCode;
import org.zafu.orderservice.mapper.OrderMapper;
import org.zafu.orderservice.model.Order;
import org.zafu.orderservice.model.OrderItem;
import org.zafu.orderservice.model.OrderStatus;
import org.zafu.orderservice.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COD-ORDER")
public class CodOrderPayment implements OrderPayment{
    private final BookClient bookClient;
    private final CartClient cartClient;
    private final PaymentClient paymentClient;
    private final OrderRepository  orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProducer orderProducer;


    @Override
    public PaymentMethod supports() {
        return PaymentMethod.COD;
    }

    @Override
    public OrderResponse process(Order order, List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            UpdateStockRequest updateStockRequest = new UpdateStockRequest();
            updateStockRequest.setQuantity(item.getBookQuantity());
            bookClient.updateStock(item.getBookId(), updateStockRequest);
        }

        if (!order.getStatus().canTransition(OrderStatus.PROCESSING)) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        paymentClient.savePayment(PaymentRequest.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(PaymentStatus.PROCESSING)
                .type(PaymentMethod.COD)
                .build());

        cartClient.clearCart(order.getUserId());

        OrderResponse response = orderMapper.toOrderResponse(order, bookClient);
        orderProducer.sendPaymentConfirmation(orderMapper.toOrderConfirmation(response));
        return response;
    }
}
