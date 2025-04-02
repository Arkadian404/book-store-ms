package org.zafu.orderservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private double price;
    private String imageUrl;
    private int stockQuantity;
}
