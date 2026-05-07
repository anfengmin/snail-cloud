package com.snail.common.storage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.OSSObject;
import com.snail.common.storage.enums.StorageType;
import com.snail.common.storage.exception.StorageException;
import com.snail.common.storage.model.StorageConfig;
import com.snail.common.storage.model.StorageUploadRequest;
import com.snail.common.storage.model.UploadResult;
import com.snail.common.storage.service.StorageClient;
import com.snail.common.storage.util.StorageEndpointUtils;
import com.snail.common.storage.util.StoragePathUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云 OSS 客户端
 *
 * @author Codex
 * @since 1.0
 */
@Slf4j
public class AliyunOssStorageClient implements StorageClient {

    @Override
    public StorageType getType() {
        return StorageType.ALIYUN;
    }

    @Override
    public UploadResult upload(StorageConfig config, StorageUploadRequest request) {
        OSS client = createClient(config);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(request.getSize());
            if (StrUtil.isNotBlank(request.getContentType())) {
                metadata.setContentType(request.getContentType());
            }
            client.putObject(config.getBucketName(), request.getObjectKey(), request.getInputStream(), metadata);
            return buildUploadResult(config, request);
        } catch (Exception e) {
            throw new StorageException("阿里云 OSS 上传失败", e);
        } finally {
            shutdown(client);
        }
    }

    @Override
    public void delete(StorageConfig config, String objectKey) {
        OSS client = createClient(config);
        try {
            client.deleteObject(config.getBucketName(), objectKey);
        } catch (Exception e) {
            throw new StorageException("阿里云 OSS 删除文件失败", e);
        } finally {
            shutdown(client);
        }
    }

    @Override
    public boolean exists(StorageConfig config, String objectKey) {
        OSS client = createClient(config);
        try {
            return client.doesObjectExist(config.getBucketName(), objectKey);
        } catch (Exception e) {
            throw new StorageException("阿里云 OSS 查询文件失败", e);
        } finally {
            shutdown(client);
        }
    }

    @Override
    public InputStream getContent(StorageConfig config, String objectKey) {
        OSS client = createClient(config);
        try {
            OSSObject object = client.getObject(config.getBucketName(), objectKey);
            return new FilterInputStream(object.getObjectContent()) {
                @Override
                public void close() throws IOException {
                    try {
                        super.close();
                    } finally {
                        shutdown(client);
                    }
                }
            };
        } catch (Exception e) {
            shutdown(client);
            throw new StorageException("阿里云 OSS 读取文件失败", e);
        }
    }

    @Override
    public String getObjectUrl(StorageConfig config, String objectKey) {
        if (StrUtil.isNotBlank(config.getDomain())) {
            return StorageEndpointUtils.withSchema(config.getDomain(), config.isHttps()) + "/" + objectKey;
        }
        String endpoint = StorageEndpointUtils.withoutSchema(config.getEndpoint());
        return StorageEndpointUtils.withSchema(config.getBucketName() + "." + endpoint, config.isHttps()) + "/" + objectKey;
    }

    private OSS createClient(StorageConfig config) {
        return new OSSClientBuilder().build(
                StorageEndpointUtils.withSchema(config.getEndpoint(), config.isHttps()),
                config.getAccessKey(),
                config.getSecretKey()
        );
    }

    private UploadResult buildUploadResult(StorageConfig config, StorageUploadRequest request) {
        return UploadResult.builder()
                .configId(config.getConfigId())
                .configKey(config.getConfigKey())
                .service(config.getType().getKey())
                .objectKey(request.getObjectKey())
                .originalFilename(request.getOriginalFilename())
                .fileName(StoragePathUtils.getFileName(request.getObjectKey()))
                .fileSuffix(StoragePathUtils.getExtension(request.getOriginalFilename()))
                .url(getObjectUrl(config, request.getObjectKey()))
                .size(request.getSize())
                .build();
    }

    private void shutdown(OSS client) {
        if (client != null) {
            try {
                client.shutdown();
            } catch (Exception e) {
                log.debug("关闭阿里云 OSS 客户端失败", e);
            }
        }
    }
}
