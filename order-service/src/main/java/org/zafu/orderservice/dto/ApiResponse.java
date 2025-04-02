package org.zafu.orderservice.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private int code = 200;
    private String message;
    private T result;
}
