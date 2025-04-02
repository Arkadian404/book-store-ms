package org.zafu.orderservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockRequest {
    private int quantity;
}
