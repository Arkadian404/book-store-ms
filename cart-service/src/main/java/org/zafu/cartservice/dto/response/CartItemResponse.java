package org.zafu.cartservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Long id;
    private Long bookId;
    private int quantity;
    private String bookTitle;
    private double bookPrice;
    private String bookImageUrl;
}
