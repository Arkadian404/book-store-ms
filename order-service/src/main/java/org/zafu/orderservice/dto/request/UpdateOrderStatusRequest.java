package org.zafu.orderservice.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.zafu.orderservice.model.OrderStatus;

@Getter
public class UpdateOrderStatusRequest {
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
