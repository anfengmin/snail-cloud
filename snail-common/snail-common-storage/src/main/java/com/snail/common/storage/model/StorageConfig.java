package com.snail.common.storage.model;

import com.snail.common.storage.enums.StorageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对象存储配置
 *
 * @author Levi
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageConfig {

    /**
     * 配置主键
     */
    private Long configId;

    /**
     * 配置标识
     */
    private String configKey;

    /**
     * 服务商类型
     */
    private StorageType type;

    /**
     * AccessKey
     */
    private String accessKey;

    /**
     * SecretKey
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 对象前缀
     */
    private String prefix;

    /**
     * 访问端点
     */
    private String endpoint;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 是否启用 https
     */
    private boolean https;

    /**
     * 区域
     */
    private String region;

    /**
     * 桶权限策略
     */
    private String accessPolicy;
}
