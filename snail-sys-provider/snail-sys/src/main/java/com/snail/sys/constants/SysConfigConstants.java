package com.snail.sys.constants;

/**
 * 系统参数常量
 */
public final class SysConfigConstants {

    private SysConfigConstants() {
    }

    /**
     * 用户初始化密码配置键
     */
    public static final String SYS_USER_INIT_PASSWORD = "sys.sysUser.initPassword";

    /**
     * 用户初始化密码默认值
     */
    public static final String SYS_USER_INIT_PASSWORD_DEFAULT = "123456";

    /**
     * 注册默认部门ID配置键
     */
    public static final String SYS_ACCOUNT_REGISTER_DEFAULT_DEPT_ID = "sys.account.register.defaultDeptId";

    /**
     * 注册默认角色标识配置键
     */
    public static final String SYS_ACCOUNT_REGISTER_DEFAULT_ROLE_KEY = "sys.account.register.defaultRoleKey";

    /**
     * 注册默认岗位编码配置键
     */
    public static final String SYS_ACCOUNT_REGISTER_DEFAULT_POST_CODE = "sys.account.register.defaultPostCode";

    /**
     * 注册默认部门ID
     */
    public static final Long SYS_ACCOUNT_REGISTER_DEFAULT_DEPT_ID_VALUE = 100L;

    /**
     * 注册默认角色标识
     */
    public static final String SYS_ACCOUNT_REGISTER_DEFAULT_ROLE_KEY_VALUE = "common";

    /**
     * 注册默认岗位编码
     */
    public static final String SYS_ACCOUNT_REGISTER_DEFAULT_POST_CODE_VALUE = "sysUser";
}
