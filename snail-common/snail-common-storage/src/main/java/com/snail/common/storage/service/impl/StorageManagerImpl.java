package com.snail.common.storage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snail.common.storage.exception.StorageException;
import com.snail.common.storage.model.StorageConfig;
import com.snail.common.storage.model.StorageUploadRequest;
import com.snail.common.storage.model.UploadResult;
import com.snail.common.storage.service.StorageClient;
import com.snail.common.storage.service.StorageConfigProvider;
import com.snail.common.storage.service.StorageManager;
import com.snail.common.storage.util.StoragePathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对象存储统一入口默认实现
 *
 * @author Levi
 * @since 1.0
 */
@Slf4j
public class StorageManagerImpl implements StorageManager {

    private final StorageConfigProvider storageConfigProvider;

    private final Map<com.snail.common.storage.enums.StorageType, StorageClient> storageClients;

    public StorageManagerImpl(StorageConfigProvider storageConfigProvider, List<StorageClient> storageClients) {
        this.storageConfigProvider = storageConfigProvider;
        this.storageClients = storageClients.stream()
                .collect(Collectors.toMap(StorageClient::getType, Function.identity()));
    }

    @Override
    public UploadResult upload(MultipartFile file) {
        return upload(null, file);
    }

    @Override
    public UploadResult upload(String configKey, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("上传文件不能为空");
        }

        StorageConfig config = resolveConfig(configKey);
        StorageClient storageClient = resolveClient(config);
        String objectKey = StoragePathUtils.buildObjectKey(config.getPrefix(), file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            return doUpload(storageClient, config, objectKey, file.getOriginalFilename(), file.getContentType(), inputStream, file.getSize());
        } catch (IOException e) {
            throw new StorageException("读取上传文件失败", e);
        }
    }

    @Override
    public UploadResult upload(String configKey, String originalFilename, String contentType, byte[] content) {
        if (content == null || content.length == 0) {
            throw new StorageException("上传文件不能为空");
        }

        StorageConfig config = resolveConfig(configKey);
        StorageClient storageClient = resolveClient(config);
        String objectKey = StoragePathUtils.buildObjectKey(config.getPrefix(), originalFilename);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
            return doUpload(storageClient, config, objectKey, originalFilename, contentType, inputStream, content.length);
        } catch (IOException e) {
            throw new StorageException("读取上传字节流失败", e);
        }
    }

    @Override
    public void delete(String objectKey) {
        delete(null, objectKey);
    }

    @Override
    public void delete(String configKey, String objectKey) {
        StorageConfig config = resolveConfig(configKey);
        resolveClient(config).delete(config, objectKey);
    }

    @Override
    public InputStream getContent(@Nullable String configKey, String objectKey) {
        StorageConfig config = resolveConfig(configKey);
        return resolveClient(config).getContent(config, objectKey);
    }

    @Override
    public String getObjectUrl(@Nullable String configKey, String objectKey) {
        StorageConfig config = resolveConfig(configKey);
        return resolveClient(config).getObjectUrl(config, objectKey);
    }

    private StorageConfig resolveConfig(@Nullable String configKey) {
        StorageConfig config = StrUtil.isBlank(configKey)
                ? storageConfigProvider.getDefaultConfig()
                : storageConfigProvider.getByConfigKey(configKey);
        if (ObjectUtil.isNull(config)) {
            throw new StorageException("未找到可用的对象存储配置");
        }
        return config;
    }

    private StorageClient resolveClient(StorageConfig config) {
        StorageClient storageClient = storageClients.get(config.getType());
        if (storageClient == null) {
            throw new StorageException("未找到存储服务商实现: " + config.getType());
        }
        return storageClient;
    }

    private UploadResult doUpload(StorageClient storageClient,
                                  StorageConfig config,
                                  String objectKey,
                                  String originalFilename,
                                  String contentType,
                                  InputStream inputStream,
                                  long size) {
        StorageUploadRequest request = StorageUploadRequest.builder()
                .objectKey(objectKey)
                .originalFilename(originalFilename)
                .contentType(contentType)
                .inputStream(inputStream)
                .size(size)
                .build();
        return storageClient.upload(config, request);
    }
}
