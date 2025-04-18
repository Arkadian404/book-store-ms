package org.zafu.userservice.dto.response.ghn;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class DistrictRequest {
    private Integer district_id;
}
