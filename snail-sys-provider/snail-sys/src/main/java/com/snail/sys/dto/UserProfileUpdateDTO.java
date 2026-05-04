package com.snail.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 当前登录用户个人资料更新参数
 */
@Data
@Schema(description = "个人资料更新参数")
public class UserProfileUpdateDTO {

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNo;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    private String avatar;

    // ========== 地址相关字段 ==========

    @Schema(description = "地址ID（更新时需要）")
    private Long addressId;

    @Schema(description = "收货人")
    private String receiverName;

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

    @Schema(description = "详细地址")
    private String detailAddress;

    @Schema(description = "地址标签（home/company/school/other）")
    private String addressTag;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "是否设为默认地址")
    private Integer isDefault;
}
