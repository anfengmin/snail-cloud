package com.snail.sys.service.impl;

import com.snail.common.log.domain.SysLoginInfo;
import com.snail.common.log.domain.SysOperateLog;
import com.snail.common.log.service.LocalLogPersistenceHandler;
import com.snail.sys.service.SysLoginInfoService;
import com.snail.sys.service.SysOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 系统模块本地日志兜底处理器
 *
 * @author Levi
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class SysLocalLogPersistenceHandler implements LocalLogPersistenceHandler {

    private final SysOperateLogService sysOperateLogService;
    private final SysLoginInfoService sysLoginInfoService;

    @Override
    public void saveOperateLog(SysOperateLog sysOperateLog) {
        sysOperateLogService.insertOperlog(sysOperateLog);
    }

    @Override
    public void saveLoginInfo(SysLoginInfo sysLoginInfo) {
        sysLoginInfoService.insertLogininfor(sysLoginInfo);
    }
}
