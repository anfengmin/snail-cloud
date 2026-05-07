package com.snail.common.log.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统访问记录
 *
 * @author makejava
 * @since 1.0
 */
@Data
@TableName("sys_login_info")
@Schema(description = "系统访问记录")
public class SysLoginInfo implements Serializable {

    private static final long serialVersionUID = 596901909022038380L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "访问ID")
    private Long id;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "登录IP地址")
    private String ipaddr;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "终端类型")
    private String clientType;

    @Schema(description = "设备标识")
    private String deviceId;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "登录状态（0成功 1失败）")
    private String status;

    @Schema(description = "提示消息")
    private String msg;

    @Schema(description = "访问时间")
    private Date loginTime;
}
