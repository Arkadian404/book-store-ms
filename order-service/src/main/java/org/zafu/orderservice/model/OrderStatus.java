package org.zafu.orderservice.model;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    PAID,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    PENDING_PAYMENT,
    CANCELLED
}
