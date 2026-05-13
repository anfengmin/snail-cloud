package com.snail.common.storage.model;

import lombok.Builder;
import lombok.Data;

/**
 * 上传结果
 *
 * @author Levi
 * @since 1.0
 */
@Data
@Builder
public class UploadResult {

    /**
     * 配置主键
     */
    private Long configId;

    /**
     * 配置标识
     */
    private String configKey;

    /**
     * 服务商标识
     */
    private String service;

    /**
     * 对象键
     */
    private String objectKey;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 生成文件名
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 文件大小
     */
    private long size;
}
