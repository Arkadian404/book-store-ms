package org.zafu.orderservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CartResponse {
    private Integer id;
    private Integer userId;
    private List<CartItemResponse> items;
}
