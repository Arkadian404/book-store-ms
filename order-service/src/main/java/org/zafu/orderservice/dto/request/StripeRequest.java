package org.zafu.orderservice.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StripeRequest {
    private Integer userId;
    private Integer orderId;
    private String orderCode;
    private String currency;
    private List<StripeItemRequest> items;
}
