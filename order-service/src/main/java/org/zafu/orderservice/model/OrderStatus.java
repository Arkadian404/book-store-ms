package org.zafu.orderservice.model;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    PAID,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    PENDING_PAYMENT,
    CANCELLED;

    public boolean canTransition(OrderStatus target) {
        return switch (this) {
            case PENDING -> target == PROCESSING || target == PENDING_PAYMENT || target == CANCELLED;
            case PENDING_PAYMENT -> target == PAID || target == CANCELLED;
            case PAID, PROCESSING -> target == CONFIRMED || target == CANCELLED;
            case CONFIRMED -> target == SHIPPED || target == CANCELLED;
            case SHIPPED -> target == DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }

}
