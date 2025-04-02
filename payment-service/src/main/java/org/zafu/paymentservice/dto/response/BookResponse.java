package org.zafu.paymentservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private double price;
    private String imageUrl;
    private int stockQuantity;
}
