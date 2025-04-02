package org.zafu.paymentservice.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class StripeRequest {
    private Integer userId;
    private Integer orderId;
    private String orderCode;
    private String currency;
    private List<StripeItemRequest> items;
}
