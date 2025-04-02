package org.zafu.notificationservice.kafka.order;

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
