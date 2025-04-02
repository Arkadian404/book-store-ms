package org.zafu.paymentservice.dto.response;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    PENDING_PAYMENT,
    PAID,
    CONFIRMED,
    SHIPPING,
    DELIVERED,
    CANCELLED
}
