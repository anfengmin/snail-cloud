-- ----------------------------
-- 1、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user
(
    id          bigint(20)  not null comment '用户ID',
    dept_id     bigint(20)   default null comment '部门ID',
    user_code   varchar(30) not null comment '用户账号',
    user_name   varchar(30) not null comment '用户名称',
    nick_name   varchar(30) not null comment '用户昵称',
    user_type   varchar(10)  default 'sys_user' comment '用户类型（sys_user系统用户）',
    email       varchar(50)  default '' comment '用户邮箱',
    phone_no    varchar(11)  default '' comment '手机号码',
    sex         char(1)      default '0' comment '用户性别（0男 1女 2未知）',
    avatar      varchar(100) default '' comment '头像地址',
    pass_word   varchar(100) default '' comment '密码',
    status      tinyint(1)   default '0' comment '帐号状态（0:正常 1:停用）',
    deleted     tinyint(1)   default '0' comment '删除标志（0:存在 1:删除）',
    login_ip    varchar(128) default '' comment '最后登录IP',
    login_date  datetime comment '最后登录时间',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id),
    unique key uk_sys_user_user_code (user_code),
    unique key uk_sys_user_email (email)
) engine = innodb comment = '用户';

-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  103, 'admin', '管理员','', 'sys_user', 'admin@163.com', '15888888888', '1', '', '$2a$10$dR4Lrr6bCjU.5Of7N9BdOOB3yERAHPoKqSS3ZdmsUAkS0.GRJZ4Eq', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '管理员');
insert into sys_user values(2,  105, 'levi', '里维','' ,'sys_user', 'levi@163.com',  '15666666666', '1', '', '$2a$10$dR4Lrr6bCjU.5Of7N9BdOOB3yERAHPoKqSS3ZdmsUAkS0.GRJZ4Eq', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '测试员');

-- ----------------------------
-- 2、部门表
-- ----------------------------

drop table if exists sys_dept;
create table sys_dept
(
    id          bigint(20) not null comment '部门id',
    parent_id   bigint(20)   default 0 comment '父部门id',
    ancestors   varchar(500) default '' comment '祖级列表',
    dept_name   varchar(30)  default '' comment '部门名称',
    order_no    int(4)       default 0 comment '显示顺序',
    leader      varchar(20)  default null comment '负责人',
    phone       varchar(11)  default null comment '联系电话',
    email       varchar(50)  default null comment '邮箱',
    status      tinyint(1)   default 0 comment '部门状态（0:正常 1:停用）',
    deleted     tinyint(1)   default 0 comment '删除标志（0:存在 1:删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id)
) engine = innodb comment = '部门';
-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept values(100,  0,   '0',          '蜗牛科技',   0, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(101,  100, '0,100',      '深圳总公司', 1, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(102,  100, '0,100',      '长沙分公司', 2, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(103,  101, '0,100,101',  '研发部门',   1, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(104,  101, '0,100,101',  '市场部门',   2, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(105,  101, '0,100,101',  '测试部门',   3, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(106,  101, '0,100,101',  '财务部门',   4, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(107,  101, '0,100,101',  '运维部门',   5, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(108,  102, '0,100,102',  '市场部门',   1, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(109,  102, '0,100,102',  '财务部门',   2, 'admin', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);

-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
    id          bigint(20)  not null comment '岗位ID',
    post_code   varchar(64) not null comment '岗位编码',
    post_name   varchar(50) not null comment '岗位名称',
    post_sort   int(4)      not null comment '显示顺序',
    status      tinyint(1)  not null comment '状态（0正常 1停用）',
    deleted     tinyint(1)   default 0 comment '删除标志（0:存在 1:删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb comment = '岗位信息';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, 0,  0, 'admin', sysdate(), '', null, '');
insert into sys_post values(2, 'se',   '项目经理',  2, 0,  0, 'admin', sysdate(), '', null, '');
insert into sys_post values(3, 'hr',   '人力资源',  3, 0,  0, 'admin', sysdate(), '', null, '');
insert into sys_post values(4, 'sysUser', '普通员工',  4, 0,  0, 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role
(
    id                  bigint(20)   not null comment '角色ID',
    role_name           varchar(30)  not null comment '角色名称',
    role_key            varchar(100) not null comment '角色权限字符串',
    role_sort           int(4)       not null comment '显示顺序',
    data_scope          char(1)      default '1' comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    menu_check_strictly tinyint(1)   default 1 comment '菜单树选择项是否关联显示',
    dept_check_strictly tinyint(1)   default 1 comment '部门树选择项是否关联显示',
    status              tinyint(1)   default 0 comment '状态（0正常 1停用）',
    deleted             tinyint(1)   default 0 comment '删除标志（0:存在 1:删除）',
    create_by           varchar(64)  default '' comment '创建者',
    create_time         datetime comment '创建时间',
    update_by           varchar(64)  default '' comment '更新者',
    update_time         datetime comment '更新时间',
    remark              varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb comment = '角色信息';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values('1', '超级管理员',  'admin',  1, 1, 1, 1, '0', '0', 'admin', sysdate(), '', null, '超级管理员');
insert into sys_role values('2', '普通角色',    'common', 2, 2, 1, 1, '0', '0', 'admin', sysdate(), '', null, '普通角色');

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu
(
    id          bigint(20)  not null comment '菜单ID',
    menu_name   varchar(50) not null comment '菜单名称',
    parent_id   bigint(20)   default 0 comment '父菜单ID',
    order_num   int(4)       default 0 comment '显示顺序',
    path        varchar(200) default '' comment '路由地址',
    name_en     varchar(50)         null comment '英文',
    component   varchar(255) default null comment '组件路径',
    query_param varchar(255) default null comment '路由参数',
    is_frame    tinyint(1)   default 1 comment '是否为外链（0是 1否）',
    is_cache    tinyint(1)   default 0 comment '是否缓存（0缓存 1不缓存）',
    menu_type   char(1)      default '' comment '菜单类型（M目录 C菜单 F按钮）',
    visible     tinyint(1)   default 0 comment '显示状态（0显示 1隐藏）',
    status      tinyint(1)   default 0 comment '状态（0正常 1停用）',
    deleted     tinyint(1)   default 0 comment '删除标志（0:存在 1:删除）',
    perms       varchar(100) default null comment '权限标识',
    icon        varchar(100) default '#' comment '菜单图标',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default '' comment '备注',
    primary key (id)
) engine = innodb comment = '菜单权限';
#     deleted     tinyint(1)   default 0 comment '删除标志（0:存在 1:删除）',

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
INSERT INTO sys_menu VALUES (1, '系统管理', 0, 1, 'system', null, '', 1, 0, 'M', '0', 0, 0, '', 'system', 'admin', sysdate(), '', null, '系统管理目录');
INSERT INTO sys_menu VALUES (2, '系统监控', 0, 2, 'monitor', null, '', 1, 0, 'M', '0', 0, 0, '', 'monitor', 'admin', sysdate(), '', null, '系统监控目录');
INSERT INTO sys_menu VALUES (3, '系统工具', 0, 3, 'tool', null, '', 1, 0, 'M', '0', 0, 0, '', 'tool', 'admin', sysdate(), '', null, '系统工具目录');
-- 4 号一级菜单预留给调度中心
-- 二级菜单
INSERT INTO sys_menu VALUES (100, '用户管理', 1, 1, 'sysUser', 'system/sysUser/index', '', 1, 0, 'C', '0', 0, 0, 'system:sysUser:list', 'sysUser', 'admin', sysdate(), '', null, '用户管理菜单');
INSERT INTO sys_menu VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', 0, 0, 'system:role:list', 'peoples', 'admin', sysdate(), '', null, '角色管理菜单');
INSERT INTO sys_menu VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', 0, 0, 'system:menu:list', 'tree-table', 'admin', sysdate(), '', null, '菜单管理菜单');
INSERT INTO sys_menu VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', 0, 0, 'system:dept:list', 'tree', 'admin', sysdate(), '', null, '部门管理菜单');
INSERT INTO sys_menu VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', 0, 0, 'system:post:list', 'post', 'admin', sysdate(), '', null, '岗位管理菜单');
INSERT INTO sys_menu VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', 0, 0, 'system:dict:list', 'dict', 'admin', sysdate(), '', null, '字典管理菜单');
INSERT INTO sys_menu VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', 0, 0, 'system:config:list', 'edit', 'admin', sysdate(), '', null, '参数设置菜单');
INSERT INTO sys_menu VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', 0, 0, 'system:notice:list', 'message', 'admin', sysdate(), '', null, '通知公告菜单');
INSERT INTO sys_menu VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', 0, 0, '', 'log', 'admin', sysdate(), '', null, '日志管理菜单');
INSERT INTO sys_menu VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', 0, 0, 'monitor:online:list', 'online', 'admin', sysdate(), '', null, '在线用户菜单');
INSERT INTO sys_menu VALUES (110, 'XxlJob控制台', 2, 2, 'http://localhost:9900', '', '', 0, 0, 'C', '0', 0, 0, 'monitor:job:list', 'job', 'admin', sysdate(), '', null, '定时任务菜单');
INSERT INTO sys_menu VALUES (111, 'Sentinel控制台', 2, 3, 'http://localhost:8718', '', '', 0, 0, 'C', '0', 0, 0, 'monitor:sentinel:list', 'sentinel', 'admin', sysdate(), '', null, '流量控制菜单');
INSERT INTO sys_menu VALUES (112, 'Nacos控制台', 2, 4, 'http://localhost:8848/nacos', '', '', 0, 0, 'C', '0', 0, 0, 'monitor:nacos:list', 'nacos', 'admin', sysdate(), '', null, '服务治理菜单');
INSERT INTO sys_menu VALUES (113, 'Admin控制台', 2, 5, 'http://localhost:9100/login', '', '', 0, 0, 'C', '0', 0, 0, 'monitor:server:list', 'server', 'admin', sysdate(), '', null, '服务监控菜单');
INSERT INTO sys_menu VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', 0, 0, 'tool:build:list', 'build', 'admin', sysdate(), '', null, '表单构建菜单');
INSERT INTO sys_menu VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', 0, 0, 'tool:gen:list', 'code', 'admin', sysdate(), '', null, '代码生成菜单');
-- oss菜单
INSERT INTO sys_menu VALUES (118, '文件管理', 1, 10, 'oss', 'system/oss/index', '', 1, 0, 'C', '0', 0, 0, 'system:oss:list', 'upload', 'admin', sysdate(), '', null, '文件管理菜单');
-- 三级菜单
INSERT INTO sys_menu VALUES (500, '操作日志', 108, 1, 'operlog', 'system/operlog/index', '', 1, 0, 'C', '0', 0, 0, 'system:operlog:list', 'form', 'admin', sysdate(), '', null, '操作日志菜单');
INSERT INTO sys_menu VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/logininfor/index', '', 1, 0, 'C', '0', 0, 0, 'system:logininfor:list', 'logininfor', 'admin', sysdate(), '', null, '登录日志菜单');
-- 用户管理按钮
INSERT INTO sys_menu VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:sysUser:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:sysUser:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:sysUser:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:sysUser:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:sysUser:export', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:sysUser:import', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:sysUser:resetPwd', '#', 'admin', sysdate(), '', null, '');
-- 角色管理按钮
INSERT INTO sys_menu VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:role:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:role:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:role:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:role:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:role:export', '#', 'admin', sysdate(), '', null, '');
-- 菜单管理按钮
INSERT INTO sys_menu VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:menu:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:menu:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:menu:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:menu:remove', '#', 'admin', sysdate(), '', null, '');
-- 部门管理按钮
INSERT INTO sys_menu VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:dept:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:dept:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:dept:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:dept:remove', '#', 'admin', sysdate(), '', null, '');
-- 岗位管理按钮
INSERT INTO sys_menu VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:post:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:post:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:post:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:post:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', 0, 0, 'system:post:export', '#', 'admin', sysdate(), '', null, '');
-- 字典管理按钮
INSERT INTO sys_menu VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:dict:query', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:dict:add', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:dict:edit', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:dict:remove', '#', 'admin', sysdate(), '', null, '');
INSERT INTO sys_menu VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:dict:export', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
-- 参数设置按钮
INSERT INTO sys_menu VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:config:query', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:config:add', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:config:edit', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:config:remove', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:config:export', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
-- 通知公告按钮
INSERT INTO sys_menu VALUES (1036, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:notice:query', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1037, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:notice:add', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1038, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:notice:edit', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1039, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:notice:remove', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
-- 操作日志按钮
INSERT INTO sys_menu VALUES (1040, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:operlog:query', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1041, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:operlog:remove', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1042, '日志导出', 500, 4, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:operlog:export', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
-- 登录日志按钮
INSERT INTO sys_menu VALUES (1043, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:logininfor:query', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1044, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:logininfor:remove', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1045, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:logininfor:export', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
-- 在线用户按钮
INSERT INTO sys_menu VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'monitor:online:query', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'monitor:online:batchLogout', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', 0, 0, 'monitor:online:forceLogout', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1050, '账户解锁', 501, 4, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:logininfor:unlock', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
-- 代码生成按钮
INSERT INTO sys_menu VALUES (1055, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'tool:gen:query', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1056, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'tool:gen:edit', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1057, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', 0, 0, 'tool:gen:remove', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1058, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'tool:gen:import', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1059, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', 0, 0, 'tool:gen:preview', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1060, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', 0, 0, 'tool:gen:code', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
-- oss相关按钮
INSERT INTO sys_menu VALUES (1600, '文件查询', 118, 1, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:oss:query', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1601, '文件上传', 118, 2, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:oss:upload', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1602, '文件下载', 118, 3, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:oss:download', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1603, '文件删除', 118, 4, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:oss:remove', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1604, '配置添加', 118, 5, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:oss:add', '#', 'admin', '2025-05-18 20:48:08', '', null, '');
INSERT INTO sys_menu VALUES (1605, '配置编辑', 118, 6, '#', '', '', 1, 0, 'F', '0', 0, 0, 'system:oss:edit', '#', 'admin', '2025-05-18 20:48:08', '', null, '');


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
                               user_id   bigint(20) not null comment '用户ID',
                               role_id   bigint(20) not null comment '角色ID',
                               primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values ('1', '1');
insert into sys_user_role values ('2', '2');


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
                               role_id   bigint(20) not null comment '角色ID',
                               menu_id   bigint(20) not null comment '菜单ID',
                               primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
insert into sys_role_menu values ('2', '1');
insert into sys_role_menu values ('2', '2');
insert into sys_role_menu values ('2', '3');
insert into sys_role_menu values ('2', '4');
insert into sys_role_menu values ('2', '100');
insert into sys_role_menu values ('2', '101');
insert into sys_role_menu values ('2', '102');
insert into sys_role_menu values ('2', '103');
insert into sys_role_menu values ('2', '104');
insert into sys_role_menu values ('2', '105');
insert into sys_role_menu values ('2', '106');
insert into sys_role_menu values ('2', '107');
insert into sys_role_menu values ('2', '108');
insert into sys_role_menu values ('2', '109');
insert into sys_role_menu values ('2', '110');
insert into sys_role_menu values ('2', '111');
insert into sys_role_menu values ('2', '112');
insert into sys_role_menu values ('2', '113');
insert into sys_role_menu values ('2', '114');
insert into sys_role_menu values ('2', '115');
insert into sys_role_menu values ('2', '116');
insert into sys_role_menu values ('2', '500');
insert into sys_role_menu values ('2', '501');
insert into sys_role_menu values ('2', '1000');
insert into sys_role_menu values ('2', '1001');
insert into sys_role_menu values ('2', '1002');
insert into sys_role_menu values ('2', '1003');
insert into sys_role_menu values ('2', '1004');
insert into sys_role_menu values ('2', '1005');
insert into sys_role_menu values ('2', '1006');
insert into sys_role_menu values ('2', '1007');
insert into sys_role_menu values ('2', '1008');
insert into sys_role_menu values ('2', '1009');
insert into sys_role_menu values ('2', '1010');
insert into sys_role_menu values ('2', '1011');
insert into sys_role_menu values ('2', '1012');
insert into sys_role_menu values ('2', '1013');
insert into sys_role_menu values ('2', '1014');
insert into sys_role_menu values ('2', '1015');
insert into sys_role_menu values ('2', '1016');
insert into sys_role_menu values ('2', '1017');
insert into sys_role_menu values ('2', '1018');
insert into sys_role_menu values ('2', '1019');
insert into sys_role_menu values ('2', '1020');
insert into sys_role_menu values ('2', '1021');
insert into sys_role_menu values ('2', '1022');
insert into sys_role_menu values ('2', '1023');
insert into sys_role_menu values ('2', '1024');
insert into sys_role_menu values ('2', '1025');
insert into sys_role_menu values ('2', '1026');
insert into sys_role_menu values ('2', '1027');
insert into sys_role_menu values ('2', '1028');
insert into sys_role_menu values ('2', '1029');
insert into sys_role_menu values ('2', '1030');
insert into sys_role_menu values ('2', '1031');
insert into sys_role_menu values ('2', '1032');
insert into sys_role_menu values ('2', '1033');
insert into sys_role_menu values ('2', '1034');
insert into sys_role_menu values ('2', '1035');
insert into sys_role_menu values ('2', '1036');
insert into sys_role_menu values ('2', '1037');
insert into sys_role_menu values ('2', '1038');
insert into sys_role_menu values ('2', '1039');
insert into sys_role_menu values ('2', '1040');
insert into sys_role_menu values ('2', '1041');
insert into sys_role_menu values ('2', '1042');
insert into sys_role_menu values ('2', '1043');
insert into sys_role_menu values ('2', '1044');
insert into sys_role_menu values ('2', '1045');
insert into sys_role_menu values ('2', '1046');
insert into sys_role_menu values ('2', '1047');
insert into sys_role_menu values ('2', '1048');
insert into sys_role_menu values ('2', '1049');
insert into sys_role_menu values ('2', '1050');
insert into sys_role_menu values ('2', '1051');
insert into sys_role_menu values ('2', '1052');
insert into sys_role_menu values ('2', '1053');
insert into sys_role_menu values ('2', '1054');
insert into sys_role_menu values ('2', '1055');
insert into sys_role_menu values ('2', '1056');
insert into sys_role_menu values ('2', '1057');
insert into sys_role_menu values ('2', '1058');
insert into sys_role_menu values ('2', '1059');
insert into sys_role_menu values ('2', '1060');


-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
                               role_id   bigint(20) not null comment '角色ID',
                               dept_id   bigint(20) not null comment '部门ID',
                               primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept values ('2', '100');
insert into sys_role_dept values ('2', '101');
insert into sys_role_dept values ('2', '105');

-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
    user_id   bigint(20) not null comment '用户ID',
    post_id   bigint(20) not null comment '岗位ID',
    primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into sys_user_post values ('1', '1');
insert into sys_user_post values ('2', '2');

-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_operate_log;
create table sys_operate_log
(
    id               bigint(20) not null comment '日志主键',
    title            varchar(50)   default '' comment '模块标题',
    business_type    tinyint(2)    default 0 comment '业务类型（0其它 1新增 2修改 3删除）',
    method           varchar(100)  default '' comment '方法名称',
    request_method   varchar(10)   default '' comment '请求方式',
    operator_type    tinyint(1)    default 0 comment '操作类别（0其它 1后台用户 2手机端用户）',
    operator_name    varchar(50)   default '' comment '操作人员',
    dept_name        varchar(50)   default '' comment '部门名称',
    operate_url      varchar(255)  default '' comment '请求URL',
    operate_ip       varchar(128)  default '' comment '主机地址',
    operate_location varchar(255)  default '' comment '操作地点',
    operate_param    varchar(2000) default '' comment '请求参数',
    json_result      varchar(2000) default '' comment '返回参数',
    status           tinyint(1)    default 0 comment '操作状态（0正常 1异常）',
    error_msg        varchar(2000) default '' comment '错误消息',
    cost_time        bigint(20)    default 0 comment '消耗时间(毫秒)',
    operate_time     datetime comment '操作时间',
    primary key (id),
    key idx_sys_oper_log_bt (business_type),
    key idx_sys_oper_log_s (status),
    key idx_sys_oper_log_ot (operate_time)
) engine = innodb comment = '操作日志记录';



-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
    id          bigint(20) not null comment '字典主键',
    dict_name   varchar(100) default '' comment '字典名称',
    dict_type   varchar(100) default '' comment '字典类型',
    status      tinyint(1)   default 0 comment '状态（0:正常 1:停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id),
    unique (dict_type)
) engine = innodb comment = '字典类型';

insert into sys_dict_type values(1,  '用户性别', 'sys_user_sex',        '0', 'admin', sysdate(), '', null, '用户性别列表');
insert into sys_dict_type values(2,  '菜单状态', 'sys_show_hide',       '0', 'admin', sysdate(), '', null, '菜单状态列表');
insert into sys_dict_type values(3,  '系统开关', 'sys_normal_disable',  '0', 'admin', sysdate(), '', null, '系统开关列表');
insert into sys_dict_type values(6,  '系统是否', 'sys_yes_no',          '0', 'admin', sysdate(), '', null, '系统是否列表');
insert into sys_dict_type values(7,  '通知类型', 'sys_notice_type',     '0', 'admin', sysdate(), '', null, '通知类型列表');
insert into sys_dict_type values(8,  '通知状态', 'sys_notice_status',   '0', 'admin', sysdate(), '', null, '通知状态列表');
insert into sys_dict_type values(9,  '操作类型', 'sys_oper_type',       '0', 'admin', sysdate(), '', null, '操作类型列表');
insert into sys_dict_type values(10, '系统状态', 'sys_common_status',   '0', 'admin', sysdate(), '', null, '登录状态列表');

-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
    id          bigint(20) not null comment '字典编码',
    dict_sort   int(4)       default 0 comment '字典排序',
    dict_label  varchar(100) default '' comment '字典标签',
    dict_value  varchar(100) default '' comment '字典键值',
    dict_type   varchar(100) default '' comment '字典类型',
    css_class   varchar(100) default null comment '样式属性（其他样式扩展）',
    list_class  varchar(100) default null comment '表格回显样式',
    is_default  char(1)      default 'N' comment '是否默认（Y是 N否）',
    status      tinyint(1)   default 0 comment '状态（0:正常 1:停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb comment = '字典数据';

insert into sys_dict_data values(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', 'admin', sysdate(), '', null, '性别男');
insert into sys_dict_data values(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别女');
insert into sys_dict_data values(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别未知');
insert into sys_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '显示菜单');
insert into sys_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '隐藏菜单');
insert into sys_dict_data values(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '系统默认是');
insert into sys_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '系统默认否');
insert into sys_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', 'admin', sysdate(), '', null, '通知');
insert into sys_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', 'admin', sysdate(), '', null, '公告');
insert into sys_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '关闭状态');
insert into sys_dict_data values(29, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '其他操作');
insert into sys_dict_data values(18, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '新增操作');
insert into sys_dict_data values(19, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '修改操作');
insert into sys_dict_data values(20, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '删除操作');
insert into sys_dict_data values(21, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '授权操作');
insert into sys_dict_data values(22, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导出操作');
insert into sys_dict_data values(23, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导入操作');
insert into sys_dict_data values(24, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '强退操作');
insert into sys_dict_data values(25, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '生成操作');
insert into sys_dict_data values(26, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '清空操作');
insert into sys_dict_data values(27, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(28, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');

-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config
(
    id           bigint(20) not null comment '参数主键',
    config_name  varchar(100) default '' comment '参数名称',
    config_key   varchar(100) default '' comment '参数键名',
    config_value varchar(500) default '' comment '参数键值',
    config_type  char(1)      default 'N' comment '系统内置（Y是 N否）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb comment = '参数配置';

insert into sys_config values(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',       'skin-blue',     'Y', 'admin', sysdate(), '', null, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' );
insert into sys_config values(2, '用户管理-账号初始密码',         'sys.sysUser.initPassword',    '123456',        'Y', 'admin', sysdate(), '', null, '初始化密码 123456' );
insert into sys_config values(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',      'theme-dark',    'Y', 'admin', sysdate(), '', null, '深色主题theme-dark，浅色主题theme-light' );
insert into sys_config values(4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false',         'Y', 'admin', sysdate(), '', null, '是否开启注册用户功能（true开启，false关闭）');
insert into sys_config values(11, 'OSS预览列表资源开关',        'sys.oss.previewListResource', 'true',      'Y', 'admin', sysdate(), '', null, 'true:开启, false:关闭');
insert into sys_config values(12, '注册默认部门ID',             'sys.account.register.defaultDeptId', '100', 'Y', 'admin', sysdate(), '', null, '注册用户默认归属部门ID');
insert into sys_config values(13, '注册默认角色标识',           'sys.account.register.defaultRoleKey', 'common', 'Y', 'admin', sysdate(), '', null, '注册用户默认角色标识');
insert into sys_config values(14, '注册默认岗位编码',           'sys.account.register.defaultPostCode', 'sysUser', 'Y', 'admin', sysdate(), '', null, '注册用户默认岗位编码');

-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_login_info;
create table sys_login_info
(
    id             bigint(20) not null comment '访问ID',
    user_name      varchar(50)  default '' comment '用户账号',
    ipaddr         varchar(128) default '' comment '登录IP地址',
    login_location varchar(255) default '' comment '登录地点',
    browser        varchar(50)  default '' comment '浏览器类型',
    os             varchar(50)  default '' comment '操作系统',
    client_type    varchar(20)  default '' comment '终端类型',
    device_id      varchar(64)  default '' comment '设备标识',
    device_name    varchar(128) default '' comment '设备名称',
    status         char(1)      default '0' comment '登录状态（0成功 1失败）',
    msg            varchar(255) default '' comment '提示消息',
    login_time     datetime comment '访问时间',
    primary key (id),
    key idx_sys_login_info_s (status),
    key idx_sys_login_info_lt (login_time)
) engine = innodb comment = '系统访问记录';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice
(
    id             bigint(20)  not null comment '公告ID',
    notice_title   varchar(50) not null comment '公告标题',
    notice_type    char(1)     not null comment '公告类型（1通知 2公告）',
    notice_content longblob     default null comment '公告内容',
    status         tinyint(1)   default 0 comment '公告状态（0:正常 1:关闭）',
    create_by      varchar(64)  default '' comment '创建者',
    create_time    datetime comment '创建时间',
    update_by      varchar(64)  default '' comment '更新者',
    update_time    datetime comment '更新时间',
    remark         varchar(255) default null comment '备注',
    primary key (id)
) engine = innodb comment = '通知公告';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into sys_notice values('1', '温馨提醒：2018-07-01 版本发布啦', '2', '新版本内容', '0', 'admin', sysdate(), '', null, '管理员');
insert into sys_notice values('2', '维护通知：2018-07-01 系统凌晨维护', '1', '维护内容',   '0', 'admin', sysdate(), '', null, '管理员');

-- ----------------------------
-- 18、OSS对象存储表
-- ----------------------------
drop table if exists sys_oss;
create table sys_oss
(
    id            bigint(20)   not null comment '对象存储主键',
    file_name     varchar(255) not null default '' comment '文件名',
    original_name varchar(255) not null default '' comment '原名',
    file_suffix   varchar(10)  not null default '' comment '文件后缀名',
    url           varchar(500) not null comment 'URL地址',
    create_time   datetime              default null comment '创建时间',
    create_by     varchar(64)           default '' comment '上传人',
    update_time   datetime              default null comment '更新时间',
    update_by     varchar(64)           default '' comment '更新人',
    service       varchar(20)  not null default 'minio' comment '服务商',
    primary key (id)
) engine = innodb comment ='OSS对象存储';

-- for AT mode you must to init this sql for you business database. the seata server not need it.
CREATE TABLE IF NOT EXISTS undo_log
(
    id            BIGINT(20)   NOT NULL COMMENT 'branch transaction id',
    xid           VARCHAR(100) NOT NULL COMMENT 'global transaction id',
    context       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    rollback_info LONGBLOB     NOT NULL COMMENT 'rollback info',
    log_status    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    log_created   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    log_modified  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY ux_undo_log (xid, id)
) ENGINE = InnoDB COMMENT ='AT transaction mode undo table';
