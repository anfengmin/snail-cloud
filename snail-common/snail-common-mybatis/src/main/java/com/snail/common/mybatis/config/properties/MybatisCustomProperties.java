package com.snail.common.mybatis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MyBatis Plus 公共配置项
 *
 * @author Levi
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "snail.mybatis")
public class MybatisCustomProperties {

    /**
     * 单页最大查询数量
     */
    private Long maxLimit = 1000L;

    /**
     * 超出总页数时是否回到第一页
     */
    private Boolean overflow = true;

    /**
     * 可选：显式指定雪花算法 workerId
     */
    private Long workerId;

    /**
     * 可选：显式指定雪花算法 datacenterId
     */
    private Long datacenterId;
}
