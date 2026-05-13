package com.snail.auth.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.snail.auth.model.LoginContextInfo;
import com.snail.common.core.utils.ServletUtils;
import com.snail.common.core.utils.ip.AddressUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 登录终端上下文解析
 *
 * @author Levi
 * @since 1.0
 */
public final class LoginContextResolver {

    public static final String HEADER_CLIENT_TYPE = "X-Client-Type";
    public static final String HEADER_DEVICE_ID = "X-Device-Id";
    public static final String HEADER_DEVICE_NAME = "X-Device-Name";

    private LoginContextResolver() {
    }

    public static LoginContextInfo resolveCurrentRequest() {
        HttpServletRequest request = ServletUtils.getRequest();
        return resolve(request);
    }

    public static LoginContextInfo resolve(HttpServletRequest request) {
        LoginContextInfo contextInfo = new LoginContextInfo();
        if (request == null) {
            contextInfo.setClientType("unknown");
            contextInfo.setDeviceId(StrUtil.EMPTY);
            contextInfo.setDeviceName("Unknown Device");
            contextInfo.setIp("127.0.0.1");
            contextInfo.setLoginLocation("内网IP");
            contextInfo.setBrowser("Unknown");
            contextInfo.setOs("Unknown");
            return contextInfo;
        }

        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String ip = ServletUtils.getClientIP(request);
        String clientType = resolveClientType(request.getHeader(HEADER_CLIENT_TYPE), userAgent);
        String browser = safeAgentName(userAgent.getBrowser().getName(), "Unknown");
        String os = safeAgentName(userAgent.getOs().getName(), "Unknown");
        String deviceId = StrUtil.blankToDefault(StrUtil.trim(request.getHeader(HEADER_DEVICE_ID)), StrUtil.EMPTY);
        String deviceName = resolveDeviceName(request.getHeader(HEADER_DEVICE_NAME), clientType, os, browser);

        contextInfo.setClientType(clientType);
        contextInfo.setDeviceId(deviceId);
        contextInfo.setDeviceName(deviceName);
        contextInfo.setIp(ip);
        contextInfo.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        contextInfo.setBrowser(browser);
        contextInfo.setOs(os);
        return contextInfo;
    }

    private static String resolveClientType(String headerClientType, UserAgent userAgent) {
        String normalized = StrUtil.blankToDefault(StrUtil.trim(headerClientType), StrUtil.EMPTY)
                .toLowerCase(Locale.ROOT);
        if (StrUtil.equalsAny(normalized, "pc", "mobile", "app", "miniapp")) {
            return normalized;
        }
        return userAgent.isMobile() ? "mobile" : "pc";
    }

    private static String resolveDeviceName(String headerDeviceName, String clientType, String os, String browser) {
        String normalized = StrUtil.trim(headerDeviceName);
        if (StrUtil.isNotBlank(normalized)) {
            return normalized;
        }
        if (StrUtil.equals(clientType, "app")) {
            return StrUtil.format("{} App", StrUtil.blankToDefault(os, "Mobile"));
        }
        if (StrUtil.equals(clientType, "miniapp")) {
            return "MiniApp";
        }
        return StrUtil.format("{} / {}", StrUtil.blankToDefault(os, "Unknown"), StrUtil.blankToDefault(browser, "Unknown"));
    }

    private static String safeAgentName(String source, String fallback) {
        if (StrUtil.isBlank(source)) {
            return fallback;
        }
        if (StrUtil.equalsAnyIgnoreCase(source, "Unknown", "unknow")) {
            return fallback;
        }
        return source;
    }
}
