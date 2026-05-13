package cn.dev33.satoken.context.dubbo.filter;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.exception.SaTokenContextException;
import cn.dev33.satoken.context.SaTokenContextDefaultImpl;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.spring.SaBeanInject;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaTokenConsts;
import com.snail.common.core.utils.SpringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

/**
 * Sa-Token Dubbo Consumer 过滤器
 *
 * 强制提前初始化 Sa-Token 元数据，避免内网鉴权场景下的元数据加载报错。
 *
 * @author Levi
 * @since 1.0
 */
@Activate(group = {CommonConstants.CONSUMER}, order = Integer.MIN_VALUE)
public class SaTokenDubboConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        SpringUtils.getBean(SaBeanInject.class);
        boolean tokenRelayed = false;

        if (SaManager.getConfig().getCheckSameToken()) {
            RpcContext.getServiceContext().setAttachment(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
        }

        try {
            if (SaManager.getSaTokenContext() != SaTokenContextDefaultImpl.defaultContext) {
                String tokenValue = StpUtil.getTokenValueNotCut();
                if (tokenValue != null) {
                    RpcContext.getServiceContext().setAttachment(SaTokenConsts.JUST_CREATED, tokenValue);
                    tokenRelayed = true;
                }
            }
        } catch (SaTokenContextException ignored) {
            tokenRelayed = false;
        }

        Result invoke = invoker.invoke(invocation);
        if (tokenRelayed) {
            try {
                StpUtil.setTokenValue(invoke.getAttachment(SaTokenConsts.JUST_CREATED_NOT_PREFIX));
            } catch (SaTokenContextException ignored) {
                // 异步线程下没有 Sa-Token 上下文时，跳过 token 回填
            }
        }
        return invoke;
    }
}
