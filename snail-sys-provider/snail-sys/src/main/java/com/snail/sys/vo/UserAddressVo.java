package com.snail.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户地址展示对象
 */
@Data
@Schema(description = "用户地址信息")
public class UserAddressVo {

    @Schema(description = "地址ID")
    private Long id;

    @Schema(description = "收货人")
    private String receiverName;

    @Schema(description = "收货人手机号")
    private String receiverPhone;

    @Schema(description = "省/直辖市名称")
    private String provinceName;

    @Schema(description = "城市名称")
    private String cityName;

    @Schema(description = "区/县名称")
    private String districtName;

    @Schema(description = "街道/乡镇名称")
    private String streetName;

    @Schema(description = "详细地址")
    private String detailAddress;

    @Schema(description = "完整地址")
    private String fullAddress;

    @Schema(description = "地址标签（home/company/school/other）")
    private String addressTag;

    @Schema(description = "是否默认地址（0否 1是）")
    private Integer isDefault;
}