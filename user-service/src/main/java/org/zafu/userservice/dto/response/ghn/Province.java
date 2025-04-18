package org.zafu.userservice.dto.response.ghn;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Province {
    private String Code;
    private Integer ProvinceID;
    private String ProvinceName;
}
