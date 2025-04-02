package org.zafu.bookservice.dto.response;

import lombok.Builder;
import lombok.Data;
import org.zafu.bookservice.models.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String slug;
    private String description;
    private String isbn;
    private Integer pageCount;
    private String imageUrl;
    private String language;
    private BigDecimal price;
    private int stockQuantity;
    private LocalDate publishedDate;
    private PublisherResponse publisher;
    private List<AuthorResponse> authors;
    private List<CategoryResponse> categories;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
