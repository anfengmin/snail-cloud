package com.snail.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 用户地址更新参数
 */
@Data
@Schema(description = "用户地址更新参数")
public class UserAddressUpdateDTO {

    @Schema(description = "地址ID（更新时需要）")
    private Long id;

    @NotBlank(message = "收货人不能为空")
    @Schema(description = "收货人")
    private String receiverName;

    @NotBlank(message = "收货人手机号不能为空")
    @Schema(description = "收货人手机号")
    private String receiverPhone;

    @Schema(description = "省/直辖市ID")
    private Long provinceId;

    @Schema(description = "省/直辖市编码")
    private String provinceCode;

    @Schema(description = "省/直辖市名称")
    private String provinceName;

    @Schema(description = "城市ID")
    private Long cityId;

    @Schema(description = "城市编码")
    private String cityCode;

    @Schema(description = "城市名称")
    private String cityName;

    @Schema(description = "区/县ID")
    private Long districtId;

    @Schema(description = "区/县编码")
    private String districtCode;

    @Schema(description = "区/县名称")
    private String districtName;

    @Schema(description = "街道/乡镇ID")
    private Long streetId;

    @Schema(description = "街道/乡镇编码")
    private String streetCode;

    @Schema(description = "街道/乡镇名称")
    private String streetName;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址不能超过200个字符")
    @Schema(description = "详细地址")
    private String detailAddress;

    @Schema(description = "地址标签（home/company/school/other）")
    private String addressTag;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "是否设为默认地址（0否 1是）")
    private Integer isDefault;
}