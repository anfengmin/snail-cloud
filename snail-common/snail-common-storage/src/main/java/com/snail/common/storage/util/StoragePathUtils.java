package com.snail.common.storage.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 对象路径工具类
 *
 * @author Levi
 * @since 1.0
 */
public final class StoragePathUtils {

    private StoragePathUtils() {
    }

    public static String buildObjectKey(String prefix, String originalFilename) {
        String extension = getExtension(originalFilename);
        String datePath = DateUtil.format(new Date(), "yyyy/MM/dd");
        String uuid = UUID.fastUUID().toString(true);
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) {
            builder.append(StrUtil.removeSuffix(prefix.trim(), "/")).append("/");
        }
        builder.append(datePath).append("/").append(uuid);
        if (StringUtils.isNotBlank(extension)) {
            builder.append(".").append(extension);
        }
        return builder.toString();
    }

    public static String getExtension(String filename) {
        return FileNameUtil.extName(StrUtil.blankToDefault(filename, ""));
    }

    public static String getFileName(String objectKey) {
        return FileNameUtil.getName(objectKey);
    }
}
