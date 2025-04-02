package org.zafu.bookservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BookExcelRequest {
    @NotBlank(message = "Title can't be blank")
    private String title;

    private String description;

    @NotBlank(message = "ISBN can't be blank")
    private String isbn;

    @Min(value = 1, message = "Page count must be positive")
    private Integer pageCount;

    private String imageUrl;

    private String language;

    @NotNull(message = "Price can't be blank")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Quantity can't be blank")
    @Min(value = 0, message = "Quantity must be positive")
    private Integer stockQuantity;

    @NotEmpty(message = "Authors can't be blank")
    private List<String> authors;

    @NotEmpty(message = "Categories can't be blank")
    private List<String> categories;

    @NotBlank(message = "Publisher can't be blank")
    private String publisher;

    @NotBlank(message = "Published date can't be blank")
    private String publishedDate;
}
