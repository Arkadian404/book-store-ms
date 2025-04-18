package org.zafu.userservice.dto.response.ghn;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ward {
    private String WardCode;
    private String WardName;
}
