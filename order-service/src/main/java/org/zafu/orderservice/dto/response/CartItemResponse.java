package org.zafu.orderservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Integer id;
    private Integer bookId;
    private String bookTitle;
    private double bookPrice;
    private String bookImageUrl;
    private Integer quantity;
}
