package org.zafu.orderservice.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.zafu.orderservice.model.OrderStatus;

@Getter
public class UpdateOrderStatusRequest {
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
