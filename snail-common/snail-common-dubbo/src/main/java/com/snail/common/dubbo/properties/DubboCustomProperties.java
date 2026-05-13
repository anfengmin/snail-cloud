package com.snail.common.dubbo.properties;

import com.snail.common.dubbo.enumd.RequestLogEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Dubbo 自定义配置
 *
 * @author Levi
 * @since 1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "dubbo.custom")
public class DubboCustomProperties {

    private Boolean requestLog = Boolean.FALSE;

    private RequestLogEnum logLevel = RequestLogEnum.INFO;
}
