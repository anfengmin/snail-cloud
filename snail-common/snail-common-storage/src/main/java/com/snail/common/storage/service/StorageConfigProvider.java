package com.snail.common.storage.service;

import com.snail.common.storage.model.StorageConfig;

/**
 * 存储配置提供者
 *
 * @author Levi
 * @since 1.0
 */
public interface StorageConfigProvider {

    /**
     * 获取默认配置
     *
     * @return 默认配置
     */
    StorageConfig getDefaultConfig();

    /**
     * 根据配置键获取配置
     *
     * @param configKey 配置键
     * @return 存储配置
     */
    StorageConfig getByConfigKey(String configKey);
}
