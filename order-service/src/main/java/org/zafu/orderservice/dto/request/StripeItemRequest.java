package org.zafu.orderservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StripeItemRequest {
    private int bookQuantity;
    private double bookPrice;
    private String bookTitle;
    private String bookImageUrl;
}
