package org.zafu.bookservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class BookRequest {
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title can't be blank")
    private String title;
    private String description;
    @NotBlank(message = "Isbn is required")
    private String isbn;
    @NotNull(message = "Page count is required")
    @Positive(message = "Page count must be positive number")
    private int pageCount;
    private String language;
    @NotNull(message = "Price is required")
    @Positive(message = "Price can't be negative or zero")
    private BigDecimal price;
    @NotNull(message = "Stock quantity is required")
    @Positive(message = "Stock quantity can't be negative or zero")
    private int stockQuantity;
    private LocalDate publishedDate;
    @NotNull(message = "Publisher is required")
    private Integer publisherId;
    @NotEmpty(message = "Categories can't be empty")
    private List<Integer> categoryIds;
    @NotEmpty(message = "Authors can't be empty")
    private List<Integer> authorIds;
}
