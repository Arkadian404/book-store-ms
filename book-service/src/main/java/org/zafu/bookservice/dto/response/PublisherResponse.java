package org.zafu.bookservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublisherResponse {
    private Integer id;
    private String name;
    private String description;
}
