package org.zafu.bookservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateStockRequest {
    @NotNull(message = "Book id is required")
    private int quantity;
}
