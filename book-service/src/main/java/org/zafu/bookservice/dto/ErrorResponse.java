package org.zafu.bookservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;
}
