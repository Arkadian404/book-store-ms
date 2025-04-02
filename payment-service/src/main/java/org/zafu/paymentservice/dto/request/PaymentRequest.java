package org.zafu.paymentservice.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.zafu.paymentservice.model.PaymentMethod;
import org.zafu.paymentservice.model.PaymentStatus;

@Getter
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
