package org.zafu.userservice.dto.response.ghn;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class District {
    private String Code;
    private Integer DistrictID;
    private String DistrictName;
}
