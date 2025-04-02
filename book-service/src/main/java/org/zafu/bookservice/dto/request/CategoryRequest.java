package org.zafu.bookservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
