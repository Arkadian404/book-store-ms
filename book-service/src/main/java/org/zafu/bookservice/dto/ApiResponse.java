package org.zafu.bookservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse <T>{
    @Builder.Default
    private int code = 200;
    private String message;
    T result;

}
