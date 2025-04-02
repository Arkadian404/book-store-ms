package org.zafu.orderservice.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
@Builder
public class PaymentRequest {
    private Integer orderId;
    private Integer userId;
    private String sessionId;
    private double totalAmount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod type;
}
