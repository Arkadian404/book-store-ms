package org.zafu.paymentservice.dto.request;

import lombok.Data;

@Data
public class StripeItemRequest {
    private int bookQuantity;
    private double bookPrice;
    private String bookTitle;
    private String bookImageUrl;
}
