package org.zafu.userservice.dto;

import lombok.*;


@Data
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private int code = 200;
    private String message;
    private T result;
}
