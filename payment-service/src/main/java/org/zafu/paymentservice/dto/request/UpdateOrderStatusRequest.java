package org.zafu.paymentservice.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import org.zafu.paymentservice.dto.response.OrderStatus;

@Data
@Builder
public class UpdateOrderStatusRequest {
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
