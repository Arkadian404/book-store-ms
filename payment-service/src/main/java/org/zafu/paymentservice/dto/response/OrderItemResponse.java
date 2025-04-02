package org.zafu.paymentservice.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Integer id;
    private Integer bookId;
    private int bookQuantity;
    private String bookTitle;
    private double bookPrice;
    private String bookImageUrl;
}
