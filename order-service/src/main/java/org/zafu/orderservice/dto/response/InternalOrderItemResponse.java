package org.zafu.orderservice.dto.response;

import lombok.Data;

@Data
public class InternalOrderItemResponse {
    private Integer id;
    private Integer bookId;
    private int bookQuantity;
    private double bookPrice;
    private String bookTitle;
    private String bookImageUrl;
}
