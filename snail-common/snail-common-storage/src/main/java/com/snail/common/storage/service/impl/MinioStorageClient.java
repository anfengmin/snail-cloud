package com.snail.common.storage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.snail.common.storage.enums.StorageType;
import com.snail.common.storage.exception.StorageException;
import com.snail.common.storage.model.StorageConfig;
import com.snail.common.storage.model.StorageUploadRequest;
import com.snail.common.storage.model.UploadResult;
import com.snail.common.storage.service.StorageClient;
import com.snail.common.storage.util.StorageEndpointUtils;
import com.snail.common.storage.util.StoragePathUtils;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;

import java.io.InputStream;

/**
 * MinIO 客户端
 *
 * @author Levi
 * @since 1.0
 */
public class MinioStorageClient implements StorageClient {

    @Override
    public StorageType getType() {
        return StorageType.MINIO;
    }

    @Override
    public UploadResult upload(StorageConfig config, StorageUploadRequest request) {
        MinioClient client = createClient(config);
        try {
            ensureBucketExists(client, config.getBucketName());
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(request.getObjectKey())
                    .stream(request.getInputStream(), request.getSize(), -1);
            if (StrUtil.isNotBlank(request.getContentType())) {
                builder.contentType(request.getContentType());
            }
            client.putObject(builder.build());
            return buildUploadResult(config, request);
        } catch (Exception e) {
            throw new StorageException("MinIO 上传文件失败", e);
        }
    }

    @Override
    public void delete(StorageConfig config, String objectKey) {
        MinioClient client = createClient(config);
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            throw new StorageException("MinIO 删除文件失败", e);
        }
    }

    @Override
    public boolean exists(StorageConfig config, String objectKey) {
        MinioClient client = createClient(config);
        try {
            client.statObject(StatObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectKey)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InputStream getContent(StorageConfig config, String objectKey) {
        MinioClient client = createClient(config);
        try {
            return client.getObject(GetObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            throw new StorageException("MinIO 读取文件失败", e);
        }
    }

    @Override
    public String getObjectUrl(StorageConfig config, String objectKey) {
        if (StrUtil.isNotBlank(config.getDomain())) {
            return StorageEndpointUtils.withSchema(config.getDomain(), config.isHttps()) + "/" + objectKey;
        }
        return StorageEndpointUtils.withSchema(config.getEndpoint(), config.isHttps())
                + "/" + config.getBucketName()
                + "/" + objectKey;
    }

    private MinioClient createClient(StorageConfig config) {
        return MinioClient.builder()
                .endpoint(StorageEndpointUtils.withSchema(config.getEndpoint(), config.isHttps()))
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }

    private void ensureBucketExists(MinioClient client, String bucketName) throws Exception {
        boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            throw new StorageException("存储桶不存在: " + bucketName);
        }
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
}
