package com.snail.common.dubbo.config;

import com.snail.common.dubbo.properties.DubboCustomProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Dubbo 公共自动配置
 *
 * @author Levi
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(DubboCustomProperties.class)
public class DubboConfiguration {
}
