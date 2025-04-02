package org.zafu.cartservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class CartResponse {
    private Integer id;
    private Integer userId;
    private List<CartItemResponse> items;
}
