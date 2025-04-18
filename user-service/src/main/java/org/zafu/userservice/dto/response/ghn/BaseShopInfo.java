package org.zafu.userservice.dto.response.ghn;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BaseShopInfo {
    @Builder.Default
    private Integer ProvinceID = 202;
    @Builder.Default
    private String ProvinceName = "Hồ Chí Minh";
    @Builder.Default
    private String Code = "3695";
    @Builder.Default
    private Integer DistrictID = 3695;
    @Builder.Default
    private String DistrictName = "Thành Phố Thủ Đức";
    @Builder.Default
    private String WardCode = "90764";
    @Builder.Default
    private String WardName = "Phường Thảo Điền";
}
