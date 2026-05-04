package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户地址簿(SysUserAddress)实体类
 *
 * @author Levi
 * @since 2026-05-04
 */
@Data
@TableName("sys_user_address")
@Schema(description = "用户地址簿")
public class SysUserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "地址ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

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

    @Schema(description = "完整地址快照")
    private String fullAddress;

    @Schema(description = "地址标签（家/公司/学校/其他）")
    private String addressTag;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "是否默认地址（0否 1是）")
    private Integer isDefault;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

    @TableLogic
    @Schema(description = "删除标志（0存在 1删除）")
    private Integer deleted;

    @Schema(description = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;
}