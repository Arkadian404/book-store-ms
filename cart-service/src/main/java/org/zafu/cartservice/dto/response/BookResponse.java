package org.zafu.cartservice.dto.response;

import lombok.Data;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private double price;
    private String imageUrl;
}
