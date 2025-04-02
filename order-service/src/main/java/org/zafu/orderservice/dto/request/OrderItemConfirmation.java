package org.zafu.orderservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemConfirmation {
    private String bookTitle;
    private int bookQuantity;
    private double bookPrice;
    private String bookImageUrl;
}
