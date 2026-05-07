package com.snail.common.storage.service;

import com.snail.common.storage.enums.StorageType;
import com.snail.common.storage.model.StorageConfig;
import com.snail.common.storage.model.StorageUploadRequest;
import com.snail.common.storage.model.UploadResult;

import java.io.InputStream;

/**
 * 服务商存储客户端
 *
 * @author Codex
 * @since 1.0
 */
public interface StorageClient {

    /**
     * 当前客户端支持的服务商
     *
     * @return 服务商类型
     */
    StorageType getType();

    /**
     * 上传文件
     *
     * @param config  配置
     * @param request 上传请求
     * @return 上传结果
     */
    UploadResult upload(StorageConfig config, StorageUploadRequest request);

    /**
     * 删除对象
     *
     * @param config    配置
     * @param objectKey 对象键
     */
    void delete(StorageConfig config, String objectKey);

    /**
     * 判断对象是否存在
     *
     * @param config    配置
     * @param objectKey 对象键
     * @return 是否存在
     */
    boolean exists(StorageConfig config, String objectKey);

    /**
     * 读取对象内容
     *
     * @param config    配置
     * @param objectKey 对象键
     * @return 对象输入流，调用方负责关闭
     */
    InputStream getContent(StorageConfig config, String objectKey);

    /**
     * 生成访问地址
     *
     * @param config    配置
     * @param objectKey 对象键
     * @return 访问地址
     */
    String getObjectUrl(StorageConfig config, String objectKey);

}
