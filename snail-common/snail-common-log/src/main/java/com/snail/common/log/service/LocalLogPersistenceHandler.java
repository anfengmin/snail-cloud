package com.snail.common.log.service;

import com.snail.common.log.domain.SysLoginInfo;
import com.snail.common.log.domain.SysOperateLog;

/**
 * 本地日志持久化兜底接口
 *
 * @author Levi
 * @since 1.0
 */
public interface LocalLogPersistenceHandler {

    /**
     * 保存操作日志
     */
    void saveOperateLog(SysOperateLog sysOperateLog);

    /**
     * 保存登录日志
     */
    void saveLoginInfo(SysLoginInfo sysLoginInfo);
}
