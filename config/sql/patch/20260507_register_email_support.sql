-- 注册邮箱化支持补丁
-- 执行前请先确认 sys_user 中不存在重复的 user_code 或 email 数据

ALTER TABLE sys_user
    ADD UNIQUE INDEX uk_sys_user_user_code (user_code),
    ADD UNIQUE INDEX uk_sys_user_email (email);

INSERT INTO sys_config (id, config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark)
VALUES (12, '注册默认部门ID', 'sys.account.register.defaultDeptId', '100', 'Y', 'admin', NOW(), '', NULL, '注册用户默认归属部门ID'),
       (13, '注册默认角色标识', 'sys.account.register.defaultRoleKey', 'common', 'Y', 'admin', NOW(), '', NULL, '注册用户默认角色标识'),
       (14, '注册默认岗位编码', 'sys.account.register.defaultPostCode', 'sysUser', 'Y', 'admin', NOW(), '', NULL, '注册用户默认岗位编码')
ON DUPLICATE KEY UPDATE
    config_name = VALUES(config_name),
    config_value = VALUES(config_value),
    config_type = VALUES(config_type),
    update_by = 'admin',
    update_time = NOW(),
    remark = VALUES(remark);
