package org.zafu.cartservice.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class AddCartItemRequest {
    @NotNull(message = "Book id is required")
    @Positive(message = "Book id must be greater than 0")
    private Integer bookId;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private int quantity;
}
