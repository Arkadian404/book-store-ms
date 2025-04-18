package org.zafu.userservice.dto.response.ghn;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GhnApiResponse <T> {
    private String code;
    private String message;
    private T data;
}
