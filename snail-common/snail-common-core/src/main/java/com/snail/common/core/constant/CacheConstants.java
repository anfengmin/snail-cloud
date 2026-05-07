package com.snail.common.core.constant;


/**
 * 缓存常量信息
 *
 * @author Anfm
 * Created time 2025/3/31
 * @since 1.0
 */
public interface CacheConstants {

    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY = "Authorization:login:token:";
    /**
     * 在线用户 redis key
     */
    String ONLINE_TOKEN_KEY = "online_tokens:";

    /**
     * loginid构造拼接字符串
     */
    String LOGINID_JOIN_CODE = ":";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 滑动验证码 redis key
     */
    String SLIDING_CAPTCHA_CODE_KEY = "sliding_captcha_codes:";

    /**
     * 邮箱验证码 redis key
     */
    String EMAIL_CODE_KEY = "email_codes:";

    /**
     * 邮箱验证码发送间隔锁 redis key
     */
    String EMAIL_CODE_SEND_LOCK_KEY = "email_code_send_lock:";

    /**
     * 参数管理 cache key
     */
    String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    String SYS_DICT_KEY = "sys_dict:";

    /**
     * 登录账户密码错误次数 redis key
     */
    String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 登录连续天数摘要 redis key
     */
    String LOGIN_STREAK_SUMMARY_KEY = "login:streak:summary:";

    /**
     * 登录日历 redis key
     */
    String LOGIN_CALENDAR_KEY = "login:calendar:";

    /**
     * 登录连续天数锁 redis key
     */
    String LOGIN_STREAK_LOCK_KEY = "login:streak:lock:";
}
