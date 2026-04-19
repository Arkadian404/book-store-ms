package org.zafu.orderservice.dto.response;

import lombok.Data;

@Data
public class InternalOrderItemResponse {
    private Integer bookId;
    private Integer quantity;
}
