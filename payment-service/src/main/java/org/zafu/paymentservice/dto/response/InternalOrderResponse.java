package org.zafu.paymentservice.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class InternalOrderResponse {
    private Integer id;
    private Integer userId;
    private String orderCode;
    private double totalAmount;
    private OrderStatus status;
    private List<InternalOrderItemResponse> items;
}
