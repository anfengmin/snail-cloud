package com.snail.common.storage.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 对象存储服务商类型
 *
 * @author Levi
 * @since 1.0
 */
@Getter
public enum StorageType {

    /**
     * 阿里云 OSS
     */
    ALIYUN("aliyun"),

    /**
     * MinIO
     */
    MINIO("minio");

    private final String key;

    StorageType(String key) {
        this.key = key;
    }

    public static StorageType resolve(String provider, String configKey, String endpoint) {
        String candidate = StrUtil.blankToDefault(provider, configKey);
        if (StrUtil.isNotBlank(candidate)) {
            String value = candidate.trim().toLowerCase();
            for (StorageType type : values()) {
                if (type.key.equals(value)) {
                    return type;
                }
            }
        }

        if (StrUtil.containsIgnoreCase(endpoint, "aliyuncs.com")) {
            return ALIYUN;
        }

        if (StrUtil.isNotBlank(endpoint)) {
            return MINIO;
        }

        throw new IllegalArgumentException("无法识别对象存储服务商类型");
    }
}
