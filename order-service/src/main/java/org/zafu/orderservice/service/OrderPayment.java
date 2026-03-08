package org.zafu.orderservice.service;

import org.zafu.orderservice.dto.request.PaymentMethod;
import org.zafu.orderservice.dto.response.OrderResponse;
import org.zafu.orderservice.model.Order;
import org.zafu.orderservice.model.OrderItem;

import java.util.List;

public interface OrderPayment {
    PaymentMethod supports();
    OrderResponse process(Order order, List<OrderItem> orderItems);
}
