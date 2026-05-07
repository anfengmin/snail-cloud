package com.snail.auth.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.snail.auth.form.RegisterBody;
import com.snail.auth.model.LoginContextInfo;
import com.snail.auth.util.LoginContextResolver;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.core.constant.Constants;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.enums.LoginType;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.core.exception.user.UserException;
import com.snail.common.core.utils.MessageUtils;
import com.snail.common.core.utils.mail.MailUtils;
import com.snail.common.log.domain.SysLoginInfo;
import com.snail.common.log.service.AsyncLogService;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.constants.SysConfigConstants;
import com.snail.sys.domain.SysDept;
import com.snail.sys.domain.SysPost;
import com.snail.sys.domain.SysRole;
import com.snail.sys.domain.SysUser;
import com.snail.sys.service.SysConfigService;
import com.snail.sys.service.SysDeptService;
import com.snail.sys.service.SysPostService;
import com.snail.sys.service.SysRoleService;
import com.snail.sys.service.SysUserService;
import com.snail.sys.vo.UserLoginStreakSummaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * 登录
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@Slf4j
@Service
public class SysLoginService {

    private static final int USER_CODE_MAX_LENGTH = 30;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysPostService sysPostService;

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private AsyncLogService asyncLogService;

    @Resource
    private UserLoginStreakService userLoginStreakService;

    /**
     * 密码最大错误次数(默认5次)
     */
    @Value("${user.password.maxRetryCount:5}")
    private Integer maxRetryCount;
    /**
     * 密码锁定时间（默认10分钟）
     */
    @Value("${user.password.lockTime:10}")
    private Integer lockTime;

    @Value("${snail.mail.register.expireMinutes:10}")
    private Integer registerEmailCodeExpireMinutes;

    @Value("${snail.mail.register.resendIntervalSeconds:60}")
    private Integer registerEmailCodeResendIntervalSeconds;

    @Value("${snail.mail.register.subject:Snail Cloud 注册验证码}")
    private String registerEmailSubject;

    /**
     * login
     *
     * @param userCode userCode
     * @param passWord passWord
     * @return java.lang.String
     * @since 1.0
     */
    public String login(String userCode, String passWord) {
        String loginAccount = StrUtil.trim(userCode);
        // 使用buildLoginUser获取完整的用户信息（包含权限）
        LoginUser userInfo = sysUserService.getUserInfo(loginAccount);
        checkLogin(LoginType.PASSWORD, userInfo.getUserCode(), () -> !BCrypt.checkpw(passWord, userInfo.getPassWord()));
        LoginContextInfo loginContextInfo = LoginContextResolver.resolveCurrentRequest();
        userInfo.setClientType(loginContextInfo.getClientType());
        userInfo.setDeviceId(loginContextInfo.getDeviceId());
        userInfo.setDeviceName(loginContextInfo.getDeviceName());
        LoginUtils.login(userInfo);
        Date loginTime = new Date();
        UserLoginStreakSummaryVo streakSummary = userLoginStreakService.recordLoginSuccess(
                userInfo.getId(),
                loginContextInfo.getIp(),
                loginTime
        );
        recordLoginInfo(
                userInfo.getUserCode(),
                Constants.LOGIN_SUCCESS,
                MessageUtils.message("user.login.success"),
                loginContextInfo,
                streakSummary.getCurrentStreakDays(),
                loginTime
        );

        return StpUtil.getTokenValue();
    }

    /**
     * 用户登出
     *
     * @since 1.0
     */
    public void logout() {
        try {
            // 获取当前登录用户并记录登出信息
            LoginUser loginUser = LoginUtils.getLoginUser();
            if (loginUser != null) {
                recordLoginInfo(
                        loginUser.getUserCode(),
                        Constants.LOGOUT,
                        MessageUtils.message("user.logout.success"),
                        LoginContextResolver.resolveCurrentRequest(),
                        null,
                        new Date()
                );
            }
            // 执行登出操作
            StpUtil.logout();
        } catch (NotLoginException e) {
            log.warn("用户登出时未检测到有效登录: {}", e.getMessage());
        } catch (Exception e) {
            log.error("用户登出异常: ", e);
        }
    }


    /**
     * checkLogin
     *
     * @param userCode userCode
     * @param supplier supplier
     * @since 1.0
     */
    private void checkLogin(LoginType loginType,String userCode, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + userCode;
        String loginFail = Constants.LOGIN_FAIL;
        RedisUtils.setCacheObject(errorKey, RedisUtils.getCacheObject(errorKey));
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                recordLoginInfo(
                        userCode,
                        loginFail,
                        MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime),
                        LoginContextResolver.resolveCurrentRequest(),
                        null,
                        new Date()
                );
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                recordLoginInfo(
                        userCode,
                        loginFail,
                        MessageUtils.message(loginType.getRetryLimitCount(), errorNumber),
                        LoginContextResolver.resolveCurrentRequest(),
                        null,
                        new Date()
                );
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }


    /**
     * register
     *
     * @param registerBody registerBody
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    public void register(RegisterBody registerBody) {
        String email = normalizeEmail(registerBody.getEmail());
        // 常见的 Hutool 组合写法
        validateRegisterEmailNotExists(email);
        validateRegisterEmailCode(email, registerBody.getEmailCode());

        String userCode = generateRegisterUserCode(email);
        String userName = resolveRegisterUserName(registerBody.getUserName(), userCode);

        SysUser sysUser = new SysUser();
        sysUser.setDeptId(resolveDefaultDeptId());
        sysUser.setRoleIds(new Long[]{resolveDefaultRoleId()});
        sysUser.setPostIds(new Long[]{resolveDefaultPostId()});
        sysUser.setUserCode(userCode);
        sysUser.setUserName(userName);
        sysUser.setEmail(email);
        sysUser.setPassWord(registerBody.getPassWord());
        sysUser.setCreateBy(userCode);
        sysUser.setUpdateBy(userCode);
        sysUser.setUserType(registerBody.getUserType());
        sysUser.setSex("2");

        boolean regFlag;
        try {
            regFlag = sysUserService.insertUser(sysUser);
        } catch (DuplicateKeyException e) {
            throw new ServiceException(MessageUtils.message("user.register.duplicate"));
        }
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        RedisUtils.deleteObject(buildRegisterEmailCodeKey(email));
        recordLoginInfo(
                userCode,
                Constants.REGISTER,
                MessageUtils.message("user.register.success"),
                LoginContextResolver.resolveCurrentRequest(),
                null,
                new Date()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendRegisterEmailCode(String email) {
        String normalizedEmail = normalizeEmail(email);
        validateRegisterEmailNotExists(normalizedEmail);

        String sendLockKey = buildRegisterEmailSendLockKey(normalizedEmail);
        boolean lockAcquired = RedisUtils.setObjectIfAbsent(
                sendLockKey,
                normalizedEmail,
                Duration.ofSeconds(registerEmailCodeResendIntervalSeconds)
        );
        if (!lockAcquired) {
            long ttlMillis = RedisUtils.getTimeToLive(sendLockKey);
            long seconds = Math.max(1L, ttlMillis / 1000L);
            throw new ServiceException(MessageUtils.message("user.register.email.send.too.frequently", seconds));
        }

        String verifyCode = RandomUtil.randomNumbers(6);
        String codeKey = buildRegisterEmailCodeKey(normalizedEmail);
        RedisUtils.setCacheObject(codeKey, verifyCode, Duration.ofMinutes(registerEmailCodeExpireMinutes));
        try {
            MailUtils.sendHtml(
                    normalizedEmail,
                    registerEmailSubject,
                    buildRegisterEmailContent(verifyCode)
            );
        } catch (Exception e) {
            log.error("发送注册邮箱验证码失败, email={}", normalizedEmail, e);
            RedisUtils.deleteObject(codeKey);
            RedisUtils.deleteObject(sendLockKey);
            throw new ServiceException(MessageUtils.message("user.register.email.send.error"));
        }
    }

    /**
     * 记录登录信息
     *
     * @param username username
     * @param status status
     * @param message message
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    public void recordLoginInfo(String username,
                                String status,
                                String message,
                                LoginContextInfo loginContextInfo,
                                Integer streakDays,
                                Date loginTime) {
        // 封装对象
        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setUserName(username);
        loginInfo.setIpaddr(loginContextInfo.getIp());
        loginInfo.setLoginLocation(loginContextInfo.getLoginLocation());
        loginInfo.setBrowser(loginContextInfo.getBrowser());
        loginInfo.setOs(loginContextInfo.getOs());
        loginInfo.setClientType(loginContextInfo.getClientType());
        loginInfo.setDeviceId(loginContextInfo.getDeviceId());
        loginInfo.setDeviceName(loginContextInfo.getDeviceName());
        loginInfo.setMsg(streakDays != null ? StrUtil.format("{}（连续登录{}天）", message, streakDays) : message);
        loginInfo.setLoginTime(loginTime);
        // 日志状态
        if (StrUtil.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginInfo.setStatus(String.valueOf(Constants.LOGIN_SUCCESS_STATUS));
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            loginInfo.setStatus(String.valueOf(Constants.LOGIN_FAIL_STATUS));
        }
        asyncLogService.saveLoginInfo(loginInfo);
    }

    /**
     * 验证邮箱地址是否不存在
     *
     * @param email 邮箱
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private void validateRegisterEmailNotExists(String email) {
        SysUser probe = new SysUser();
        probe.setEmail(email);
        if (sysUserService.checkEmailExists(probe)) {
            throw new ServiceException(MessageUtils.message("user.register.email.exists"));
        }
    }

    private void validateRegisterEmailCode(String email, String emailCode) {
        String cachedCode = RedisUtils.getCacheObject(buildRegisterEmailCodeKey(email));
        if (StrUtil.isBlank(cachedCode)) {
            throw new ServiceException(MessageUtils.message("user.register.email.code.expire"));
        }
        if (!StrUtil.equalsIgnoreCase(StrUtil.trim(emailCode), cachedCode)) {
            throw new ServiceException(MessageUtils.message("user.register.email.code.invalid"));
        }
    }

    /**
     * 标准化邮箱地址：去除首尾空格并转换为小写
     *
     * @param email 邮箱
     * @return 正常化邮箱
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private String normalizeEmail(String email) {
        return StrUtil.trim(email).toLowerCase(Locale.ROOT);
    }

    private String resolveRegisterUserName(String rawUserName, String userCode) {
        String userName = StrUtil.blankToDefault(StrUtil.trim(rawUserName), userCode);
        return StrUtil.maxLength(userName, USER_CODE_MAX_LENGTH);
    }

    private String generateRegisterUserCode(String email) {
        String prefix = StrUtil.subBefore(email, "@", false);
        String normalizedPrefix = sanitizeUserCodePrefix(prefix);
        String base = StrUtil.maxLength(normalizedPrefix, USER_CODE_MAX_LENGTH);
        String candidate = base;
        int suffix = 1;
        while (existsUserCode(candidate)) {
            String suffixText = String.valueOf(suffix++);
            candidate = StrUtil.maxLength(base, USER_CODE_MAX_LENGTH - suffixText.length()) + suffixText;
        }
        return candidate;
    }

    private String sanitizeUserCodePrefix(String prefix) {
        String candidate = StrUtil.blankToDefault(StrUtil.trim(prefix), "user")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]", "_")
                .replaceAll("_+", "_");
        return StrUtil.blankToDefault(candidate, "user");
    }

    private boolean existsUserCode(String userCode) {
        SysUser probe = new SysUser();
        probe.setUserCode(userCode);
        return sysUserService.checkUserCodeUnique(probe);
    }

    private Long resolveDefaultDeptId() {
        String deptIdConfig = sysConfigService.selectConfigByKey(SysConfigConstants.SYS_ACCOUNT_REGISTER_DEFAULT_DEPT_ID);
        Long configuredDeptId = null;
        if (StrUtil.isNotBlank(deptIdConfig) && StrUtil.isNumeric(deptIdConfig)) {
            configuredDeptId = Long.parseLong(deptIdConfig);
        }

        if (configuredDeptId != null) {
            boolean exists = sysDeptService.lambdaQuery()
                    .eq(SysDept::getId, configuredDeptId)
                    .eq(SysDept::getDeleted, UserConstants.USER_NORMAL)
                    .eq(SysDept::getStatus, UserConstants.USER_NORMAL)
                    .exists();
            if (exists) {
                return configuredDeptId;
            }
        }

        SysDept defaultDept = sysDeptService.lambdaQuery()
                .eq(SysDept::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysDept::getStatus, UserConstants.USER_NORMAL)
                .eq(SysDept::getDeptName, "普通部门")
                .one();
        if (ObjectUtil.isNotNull(defaultDept)) {
            return defaultDept.getId();
        }

        defaultDept = sysDeptService.lambdaQuery()
                .eq(SysDept::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysDept::getStatus, UserConstants.USER_NORMAL)
                .orderByAsc(SysDept::getParentId, SysDept::getOrderNo, SysDept::getId)
                .last("limit 1")
                .one();
        if (ObjectUtil.isNull(defaultDept)) {
            throw new ServiceException(MessageUtils.message("user.register.default.dept.not.exists"));
        }
        return defaultDept.getId();
    }

    private Long resolveDefaultRoleId() {
        String roleKey = StrUtil.blankToDefault(
                StrUtil.trim(sysConfigService.selectConfigByKey(SysConfigConstants.SYS_ACCOUNT_REGISTER_DEFAULT_ROLE_KEY)),
                SysConfigConstants.SYS_ACCOUNT_REGISTER_DEFAULT_ROLE_KEY_VALUE
        );
        SysRole role = sysRoleService.lambdaQuery()
                .eq(SysRole::getRoleKey, roleKey)
                .eq(SysRole::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysRole::getStatus, UserConstants.USER_NORMAL)
                .one();
        if (ObjectUtil.isNull(role)) {
            role = sysRoleService.lambdaQuery()
                    .eq(SysRole::getRoleName, "普通角色")
                    .eq(SysRole::getDeleted, UserConstants.USER_NORMAL)
                    .eq(SysRole::getStatus, UserConstants.USER_NORMAL)
                    .one();
        }
        if (ObjectUtil.isNull(role)) {
            throw new ServiceException(MessageUtils.message("user.register.default.role.not.exists"));
        }
        return role.getId();
    }

    private Long resolveDefaultPostId() {
        String postCode = StrUtil.blankToDefault(
                StrUtil.trim(sysConfigService.selectConfigByKey(SysConfigConstants.SYS_ACCOUNT_REGISTER_DEFAULT_POST_CODE)),
                SysConfigConstants.SYS_ACCOUNT_REGISTER_DEFAULT_POST_CODE_VALUE
        );
        SysPost post = sysPostService.lambdaQuery()
                .eq(SysPost::getPostCode, postCode)
                .eq(SysPost::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysPost::getStatus, UserConstants.USER_NORMAL)
                .one();
        if (ObjectUtil.isNull(post)) {
            post = sysPostService.lambdaQuery()
                    .eq(SysPost::getPostName, "普通员工")
                    .eq(SysPost::getDeleted, UserConstants.USER_NORMAL)
                    .eq(SysPost::getStatus, UserConstants.USER_NORMAL)
                    .one();
        }
        if (ObjectUtil.isNull(post)) {
            throw new ServiceException(MessageUtils.message("user.register.default.post.not.exists"));
        }
        return post.getId();
    }

    private String buildRegisterEmailCodeKey(String email) {
        return CacheConstants.EMAIL_CODE_KEY + email;
    }

    private String buildRegisterEmailSendLockKey(String email) {
        return CacheConstants.EMAIL_CODE_SEND_LOCK_KEY + email;
    }

    private String buildRegisterEmailContent(String verifyCode) {
        return "<div style='margin:0;padding:32px;background:#f5f7fb;font-family:PingFang SC,Microsoft YaHei,sans-serif;'>"
                + "<div style='max-width:560px;margin:0 auto;background:#ffffff;border-radius:16px;padding:32px;"
                + "box-shadow:0 12px 32px rgba(15,23,42,0.08);'>"
                + "<div style='font-size:24px;font-weight:600;color:#1f2937;margin-bottom:12px;'>Snail Cloud</div>"
                + "<div style='font-size:16px;color:#111827;margin-bottom:8px;'>您好，您正在进行邮箱注册。</div>"
                + "<div style='font-size:14px;color:#6b7280;line-height:1.8;margin-bottom:24px;'>"
                + "请在页面中输入下面的验证码完成注册，该验证码 "
                + registerEmailCodeExpireMinutes
                + " 分钟内有效。</div>"
                + "<div style='padding:18px 20px;border-radius:12px;background:#eff6ff;color:#2563eb;"
                + "font-size:32px;font-weight:700;letter-spacing:8px;text-align:center;margin-bottom:24px;'>"
                + verifyCode
                + "</div>"
                + "<div style='font-size:13px;color:#9ca3af;line-height:1.8;'>"
                + "如果这不是您的操作，请忽略这封邮件。为了账号安全，请不要将验证码泄露给他人。"
                + "</div></div></div>";
    }

}
