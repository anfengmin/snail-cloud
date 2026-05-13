package com.snail.common.storage.util;

import cn.hutool.core.util.StrUtil;

/**
 * 存储端点工具类
 *
 * @author Levi
 * @since 1.0
 */
public final class StorageEndpointUtils {

    private StorageEndpointUtils() {
    }

    public static String withSchema(String endpoint, boolean https) {
        if (StrUtil.isBlank(endpoint)) {
            return endpoint;
        }
        if (StrUtil.startWithAnyIgnoreCase(endpoint, "http://", "https://")) {
            return endpoint;
        }
        return (https ? "https://" : "http://") + endpoint;
    }

    public static String withoutSchema(String endpoint) {
        if (StrUtil.isBlank(endpoint)) {
            return endpoint;
        }
        return StrUtil.removePrefixIgnoreCase(StrUtil.removePrefixIgnoreCase(endpoint, "https://"), "http://");
    }
}
