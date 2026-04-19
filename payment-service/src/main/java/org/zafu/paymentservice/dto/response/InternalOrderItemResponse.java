package org.zafu.paymentservice.dto.response;

import lombok.Data;

@Data
public class InternalOrderItemResponse {
    private Integer bookId;
    private Integer quantity;
}