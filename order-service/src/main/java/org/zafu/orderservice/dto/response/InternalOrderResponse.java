package org.zafu.orderservice.dto.response;

import lombok.Data;
import org.zafu.orderservice.model.OrderStatus;

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
