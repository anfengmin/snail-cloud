package com.snail.auth.model;

import lombok.Data;

/**
 * 登录终端上下文
 *
 * @author Levi
 * @since 1.0
 */
@Data
public class LoginContextInfo {

    /**
     * 终端类型 pc/mobile/app/miniapp/unknown
     */
    private String clientType;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 设备展示名称
     */
    private String deviceName;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;
}
