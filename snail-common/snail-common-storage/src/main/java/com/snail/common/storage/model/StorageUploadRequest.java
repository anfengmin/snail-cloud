package com.snail.common.storage.model;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

/**
 * 对象存储上传请求
 *
 * @author Levi
 * @since 1.0
 */
@Data
@Builder
public class StorageUploadRequest {

    /**
     * 对象键
     */
    private String objectKey;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 文件流
     */
    private InputStream inputStream;

    /**
     * 文件大小
     */
    private long size;
}
