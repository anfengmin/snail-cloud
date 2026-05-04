package com.snail.common.storage.service;

import com.snail.common.storage.model.UploadResult;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * 对象存储统一入口
 *
 * @author Codex
 * @since 1.0
 */
public interface StorageManager {

    /**
     * 使用默认配置上传
     *
     * @param file 文件
     * @return 上传结果
     */
    UploadResult upload(MultipartFile file);

    /**
     * 使用指定配置上传
     *
     * @param configKey 配置键
     * @param file      文件
     * @return 上传结果
     */
    UploadResult upload(String configKey, MultipartFile file);

    /**
     * 使用指定配置上传字节内容
     *
     * @param configKey 配置键，为空时使用默认配置
     * @param originalFilename 原始文件名
     * @param contentType 内容类型
     * @param content 文件字节
     * @return 上传结果
     */
    UploadResult upload(String configKey, String originalFilename, String contentType, byte[] content);

    /**
     * 使用默认配置删除对象
     *
     * @param objectKey 对象键
     */
    void delete(String objectKey);

    /**
     * 使用指定配置删除对象
     *
     * @param configKey 配置键
     * @param objectKey 对象键
     */
    void delete(String configKey, String objectKey);

    /**
     * 生成指定配置下的对象访问地址
     *
     * @param configKey 配置键，为空时使用默认配置
     * @param objectKey 对象键
     * @return 访问地址
     */
    String getObjectUrl(@Nullable String configKey, String objectKey);

}
