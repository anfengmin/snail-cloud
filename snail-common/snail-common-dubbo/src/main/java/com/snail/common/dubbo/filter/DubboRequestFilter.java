package com.snail.common.dubbo.filter;

import cn.hutool.json.JSONUtil;
import com.snail.common.core.utils.SpringUtils;
import com.snail.common.dubbo.enumd.RequestLogEnum;
import com.snail.common.dubbo.properties.DubboCustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * Dubbo 请求日志过滤器
 *
 * @author Levi
 * @since 1.0
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, order = Integer.MAX_VALUE)
public class DubboRequestFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        DubboCustomProperties properties = SpringUtils.getBean(DubboCustomProperties.class);
        if (!Boolean.TRUE.equals(properties.getRequestLog())) {
            return invoker.invoke(invocation);
        }

        String client = RpcContext.getServiceContext().isConsumerSide() ? CommonConstants.CONSUMER : CommonConstants.PROVIDER;
        String baseLog = "Client[" + client + "],InterfaceName=[" + invocation.getInvoker().getInterface().getSimpleName()
                + "],MethodName=[" + invocation.getMethodName() + "]";

        if (properties.getLogLevel() == RequestLogEnum.INFO) {
            log.info("DUBBO - 服务调用: {}", baseLog);
        } else {
            log.info("DUBBO - 服务调用: {},Parameter={}", baseLog, JSONUtil.toJsonStr(invocation.getArguments()));
        }

        long startTime = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - startTime;

        if (result.hasException() && invoker.getInterface().equals(GenericService.class)) {
            log.error("DUBBO - 服务异常: {},Exception={}", baseLog, result.getException().getMessage(), result.getException());
        } else if (properties.getLogLevel() == RequestLogEnum.INFO) {
            log.info("DUBBO - 服务响应: {},SpendTime=[{}ms]", baseLog, elapsed);
        } else if (properties.getLogLevel() == RequestLogEnum.FULL) {
            log.info("DUBBO - 服务响应: {},SpendTime=[{}ms],Response={}", baseLog, elapsed,
                    JSONUtil.toJsonStr(result.getValue()));
        }

        return result;
    }
}
