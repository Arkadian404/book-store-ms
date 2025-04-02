package org.zafu.bookservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
public class PageResponse <T>{
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private Long totalElements;
    @Builder.Default
    private List<T> data = Collections.emptyList();
}
