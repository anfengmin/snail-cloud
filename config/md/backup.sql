-- MySQL dump 10.13  Distrib 8.0.33, for macos13 (x86_64)
--
-- Host: 127.0.0.1    Database: snail_sys
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2025-05-21 21:44:06','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.sysUser.initPassword','123456','Y','admin','2025-05-21 21:44:06','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2025-05-21 21:44:06','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2025-05-21 21:44:06','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(11,'OSS预览列表资源开关','sys.oss.previewListResource','true','Y','admin','2025-05-21 21:44:06','',NULL,'true:开启, false:关闭'),(12,'注册默认部门ID','sys.account.register.defaultDeptId','100','Y','admin','2025-05-21 21:44:06','',NULL,'注册用户默认归属部门ID'),(13,'注册默认角色标识','sys.account.register.defaultRoleKey','common','Y','admin','2025-05-21 21:44:06','',NULL,'注册用户默认角色标识'),(14,'注册默认岗位编码','sys.account.register.defaultPostCode','sysUser','Y','admin','2025-05-21 21:44:06','',NULL,'注册用户默认岗位编码');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(500) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_no` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT '0' COMMENT '部门状态（0:正常 1:停用）',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0:存在 1:删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','蜗牛科技',0,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(101,100,'0,100','深圳总公司',1,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(102,100,'0,100','长沙分公司',2,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(103,101,'0,100,101','研发部门',1,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(104,101,'0,100,101','市场部门',2,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(105,101,'0,100,101','测试部门',3,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(106,101,'0,100,101','财务部门',4,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(107,101,'0,100,101','运维部门',5,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(108,102,'0,100,102','市场部门',1,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL),(109,102,'0,100,102','财务部门',2,'admin','15888888888','ry@qq.com',0,0,'admin','2025-05-21 21:44:00','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `id` bigint NOT NULL COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0:正常 1:停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y',0,'admin','2025-05-21 21:44:06','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N',0,'admin','2025-05-21 21:44:06','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N',0,'admin','2025-05-21 21:44:06','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y',0,'admin','2025-05-21 21:44:06','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y',0,'admin','2025-05-21 21:44:06','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'停用状态'),(12,1,'是','Y','sys_yes_no','','primary','Y',0,'admin','2025-05-21 21:44:06','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y',0,'admin','2025-05-21 21:44:06','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N',0,'admin','2025-05-21 21:44:06','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y',0,'admin','2025-05-21 21:44:06','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'关闭状态'),(18,1,'新增','1','sys_oper_type','','info','N',0,'admin','2025-05-21 21:44:06','',NULL,'新增操作'),(19,2,'修改','2','sys_oper_type','','info','N',0,'admin','2025-05-21 21:44:06','',NULL,'修改操作'),(20,3,'删除','3','sys_oper_type','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'删除操作'),(21,4,'授权','4','sys_oper_type','','primary','N',0,'admin','2025-05-21 21:44:06','',NULL,'授权操作'),(22,5,'导出','5','sys_oper_type','','warning','N',0,'admin','2025-05-21 21:44:06','',NULL,'导出操作'),(23,6,'导入','6','sys_oper_type','','warning','N',0,'admin','2025-05-21 21:44:06','',NULL,'导入操作'),(24,7,'强退','7','sys_oper_type','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'强退操作'),(25,8,'生成代码','8','sys_oper_type','','warning','N',0,'admin','2025-05-21 21:44:06','',NULL,'生成操作'),(26,9,'清空数据','9','sys_oper_type','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'清空操作'),(27,1,'成功','0','sys_common_status','','primary','N',0,'admin','2025-05-21 21:44:06','',NULL,'正常状态'),(28,2,'失败','1','sys_common_status','','danger','N',0,'admin','2025-05-21 21:44:06','',NULL,'停用状态'),(29,99,'其他','0','sys_oper_type','','info','N',0,'admin','2025-05-21 21:44:06','',NULL,'其他操作');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0:正常 1:停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex',0,'admin','2025-05-21 21:44:05','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide',0,'admin','2025-05-21 21:44:06','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable',0,'admin','2025-05-21 21:44:06','',NULL,'系统开关列表'),(6,'系统是否','sys_yes_no',0,'admin','2025-05-21 21:44:06','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type',0,'admin','2025-05-21 21:44:06','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status',0,'admin','2025-05-21 21:44:06','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type',0,'admin','2025-05-21 21:44:06','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status',0,'admin','2025-05-21 21:44:06','',NULL,'登录状态列表');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_login_info`
--

DROP TABLE IF EXISTS `sys_login_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_login_info` (
  `id` bigint NOT NULL COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `client_type` varchar(20) DEFAULT '' COMMENT '终端类型',
  `device_id` varchar(64) DEFAULT '' COMMENT '设备标识',
  `device_name` varchar(128) DEFAULT '' COMMENT '设备名称',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_login_info_s` (`status`),
  KEY `idx_sys_login_info_lt` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_login_info`
--

LOCK TABLES `sys_login_info` WRITE;
/*!40000 ALTER TABLE `sys_login_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_login_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `name_en` varchar(50) DEFAULT NULL COMMENT '英文',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` tinyint(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` tinyint(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` tinyint(1) DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0:存在 1:删除）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,1,'manage',NULL,NULL,'',1,0,'M',0,0,0,'','mdi:folder-cog','admin','2025-05-21 21:44:01','',NULL,'系统管理目录'),(2,'系统监控',0,2,'monitor',NULL,NULL,'',1,0,'M',0,0,0,'','monitor','admin','2025-05-21 21:44:01','',NULL,'系统监控目录'),(3,'系统工具',0,3,'tool',NULL,NULL,'',1,0,'M',0,0,1,'','tool','admin','2025-05-21 21:44:01','',NULL,'系统工具目录'),(100,'用户管理',1,1,'user',NULL,'manage/user/index','',1,0,'C',0,0,0,'system:sysUser:list','mdi:account ','admin','2025-05-21 21:44:01','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role',NULL,'manage/role/index','',1,0,'C',0,0,0,'system:role:list','mdi:account-cog','admin','2025-05-21 21:44:01','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu',NULL,'manage/menu/index','',1,0,'C',0,0,0,'system:menu:list','mdi:menu ','admin','2025-05-21 21:44:01','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept',NULL,'manage/dept/index','',1,0,'C',0,0,1,'system:dept:list','tree','admin','2025-05-21 21:44:01','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post',NULL,'manage/post/index','',1,0,'C',0,0,1,'system:post:list','post','admin','2025-05-21 21:44:01','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict',NULL,'manage/dict/index','',1,0,'C',0,0,1,'system:dict:list','dict','admin','2025-05-21 21:44:01','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config',NULL,'manage/config/index','',1,0,'C',0,0,1,'system:config:list','edit','admin','2025-05-21 21:44:01','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice',NULL,'manage/notice/index','',1,0,'C',0,0,1,'system:notice:list','message','admin','2025-05-21 21:44:01','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log',NULL,'','',1,0,'M',0,0,1,'','log','admin','2025-05-21 21:44:01','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online',NULL,'monitor/online/index','',1,0,'C',0,0,0,'monitor:online:list','online','admin','2025-05-21 21:44:01','',NULL,'在线用户菜单'),(110,'XxlJob控制台',2,2,'http://localhost:9900',NULL,'','',0,0,'C',0,0,1,'monitor:job:list','job','admin','2025-05-21 21:44:01','',NULL,'定时任务菜单'),(111,'Sentinel控制台',2,3,'http://localhost:8718',NULL,'','',0,0,'C',0,0,1,'monitor:sentinel:list','sentinel','admin','2025-05-21 21:44:01','',NULL,'流量控制菜单'),(112,'Nacos控制台',2,4,'http://localhost:8848/nacos',NULL,'','',0,0,'C',0,0,1,'monitor:nacos:list','nacos','admin','2025-05-21 21:44:01','',NULL,'服务治理菜单'),(113,'Admin控制台',2,5,'http://localhost:9100/login',NULL,'','',0,0,'C',0,0,1,'monitor:server:list','server','admin','2025-05-21 21:44:01','',NULL,'服务监控菜单'),(114,'表单构建',3,1,'build',NULL,'tool/build/index','',1,0,'C',0,0,1,'tool:build:list','build','admin','2025-05-21 21:44:01','',NULL,'表单构建菜单'),(115,'代码生成',3,2,'gen',NULL,'tool/gen/index','',1,0,'C',0,0,1,'tool:gen:list','code','admin','2025-05-21 21:44:02','',NULL,'代码生成菜单'),(118,'文件管理',1,10,'oss',NULL,'system/oss/index','',1,0,'C',0,0,1,'system:oss:list','upload','admin','2025-05-21 21:44:02','',NULL,'文件管理菜单'),(500,'操作日志',108,1,'operlog',NULL,'system/operlog/index','',1,0,'C',0,0,1,'system:operlog:list','form','admin','2025-05-21 21:44:02','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor',NULL,'system/logininfor/index','',1,0,'C',0,0,1,'system:logininfor:list','logininfor','admin','2025-05-21 21:44:02','',NULL,'登录日志菜单'),(1001,'用户查询',100,1,'',NULL,'','',1,0,'F',0,0,1,'system:sysUser:query','#','admin','2025-05-21 21:44:02','',NULL,''),(1002,'用户新增',100,2,'',NULL,'','',1,0,'F',0,0,1,'system:sysUser:add','#','admin','2025-05-21 21:44:02','',NULL,''),(1003,'用户修改',100,3,'',NULL,'','',1,0,'F',0,0,1,'system:sysUser:edit','#','admin','2025-05-21 21:44:02','',NULL,''),(1004,'用户删除',100,4,'',NULL,'','',1,0,'F',0,0,1,'system:sysUser:remove','#','admin','2025-05-21 21:44:02','',NULL,''),(1005,'用户导出',100,5,'',NULL,'','',1,0,'F',0,0,1,'system:sysUser:export','#','admin','2025-05-21 21:44:02','',NULL,''),(1006,'用户导入',100,6,'',NULL,'','',1,0,'F',0,0,1,'system:sysUser:import','#','admin','2025-05-21 21:44:02','',NULL,''),(1007,'重置密码',100,7,'',NULL,'','',1,0,'F',0,0,1,'system:sysUser:resetPwd','#','admin','2025-05-21 21:44:02','',NULL,''),(1008,'角色查询',101,1,'',NULL,'','',1,0,'F',0,0,1,'system:role:query','#','admin','2025-05-21 21:44:02','',NULL,''),(1009,'角色新增',101,2,'',NULL,'','',1,0,'F',0,0,1,'system:role:add','#','admin','2025-05-21 21:44:02','',NULL,''),(1010,'角色修改',101,3,'',NULL,'','',1,0,'F',0,0,1,'system:role:edit','#','admin','2025-05-21 21:44:02','',NULL,''),(1011,'角色删除',101,4,'',NULL,'','',1,0,'F',0,0,1,'system:role:remove','#','admin','2025-05-21 21:44:02','',NULL,''),(1012,'角色导出',101,5,'',NULL,'','',1,0,'F',0,0,1,'system:role:export','#','admin','2025-05-21 21:44:02','',NULL,''),(1013,'菜单查询',102,1,'',NULL,'','',1,0,'F',0,0,1,'system:menu:query','#','admin','2025-05-21 21:44:02','',NULL,''),(1014,'菜单新增',102,2,'',NULL,'','',1,0,'F',0,0,1,'system:menu:add','#','admin','2025-05-21 21:44:02','',NULL,''),(1015,'菜单修改',102,3,'',NULL,'','',1,0,'F',0,0,1,'system:menu:edit','#','admin','2025-05-21 21:44:02','',NULL,''),(1016,'菜单删除',102,4,'',NULL,'','',1,0,'F',0,0,1,'system:menu:remove','#','admin','2025-05-21 21:44:02','',NULL,''),(1017,'部门查询',103,1,'',NULL,'','',1,0,'F',0,0,1,'system:dept:query','#','admin','2025-05-21 21:44:02','',NULL,''),(1018,'部门新增',103,2,'',NULL,'','',1,0,'F',0,0,1,'system:dept:add','#','admin','2025-05-21 21:44:02','',NULL,''),(1019,'部门修改',103,3,'',NULL,'','',1,0,'F',0,0,1,'system:dept:edit','#','admin','2025-05-21 21:44:02','',NULL,''),(1020,'部门删除',103,4,'',NULL,'','',1,0,'F',0,0,1,'system:dept:remove','#','admin','2025-05-21 21:44:02','',NULL,''),(1021,'岗位查询',104,1,'',NULL,'','',1,0,'F',0,0,1,'system:post:query','#','admin','2025-05-21 21:44:02','',NULL,''),(1022,'岗位新增',104,2,'',NULL,'','',1,0,'F',0,0,1,'system:post:add','#','admin','2025-05-21 21:44:02','',NULL,''),(1023,'岗位修改',104,3,'',NULL,'','',1,0,'F',0,0,1,'system:post:edit','#','admin','2025-05-21 21:44:02','',NULL,''),(1024,'岗位删除',104,4,'',NULL,'','',1,0,'F',0,0,1,'system:post:remove','#','admin','2025-05-21 21:44:02','',NULL,''),(1025,'岗位导出',104,5,'',NULL,'','',1,0,'F',0,0,1,'system:post:export','#','admin','2025-05-21 21:44:02','',NULL,''),(1026,'字典查询',105,1,'#',NULL,'','',1,0,'F',0,0,1,'system:dict:query','#','admin','2025-05-21 21:44:02','',NULL,''),(1027,'字典新增',105,2,'#',NULL,'','',1,0,'F',0,0,1,'system:dict:add','#','admin','2025-05-21 21:44:02','',NULL,''),(1028,'字典修改',105,3,'#',NULL,'','',1,0,'F',0,0,1,'system:dict:edit','#','admin','2025-05-21 21:44:02','',NULL,''),(1029,'字典删除',105,4,'#',NULL,'','',1,0,'F',0,0,1,'system:dict:remove','#','admin','2025-05-21 21:44:02','',NULL,''),(1030,'字典导出',105,5,'#',NULL,'','',1,0,'F',0,0,1,'system:dict:export','#','admin','2025-05-18 20:48:08','',NULL,''),(1031,'参数查询',106,1,'#',NULL,'','',1,0,'F',0,0,1,'system:config:query','#','admin','2025-05-18 20:48:08','',NULL,''),(1032,'参数新增',106,2,'#',NULL,'','',1,0,'F',0,0,1,'system:config:add','#','admin','2025-05-18 20:48:08','',NULL,''),(1033,'参数修改',106,3,'#',NULL,'','',1,0,'F',0,0,1,'system:config:edit','#','admin','2025-05-18 20:48:08','',NULL,''),(1034,'参数删除',106,4,'#',NULL,'','',1,0,'F',0,0,1,'system:config:remove','#','admin','2025-05-18 20:48:08','',NULL,''),(1035,'参数导出',106,5,'#',NULL,'','',1,0,'F',0,0,1,'system:config:export','#','admin','2025-05-18 20:48:08','',NULL,''),(1036,'公告查询',107,1,'#',NULL,'','',1,0,'F',0,0,1,'system:notice:query','#','admin','2025-05-18 20:48:08','',NULL,''),(1037,'公告新增',107,2,'#',NULL,'','',1,0,'F',0,0,1,'system:notice:add','#','admin','2025-05-18 20:48:08','',NULL,''),(1038,'公告修改',107,3,'#',NULL,'','',1,0,'F',0,0,1,'system:notice:edit','#','admin','2025-05-18 20:48:08','',NULL,''),(1039,'公告删除',107,4,'#',NULL,'','',1,0,'F',0,0,1,'system:notice:remove','#','admin','2025-05-18 20:48:08','',NULL,''),(1040,'操作查询',500,1,'#',NULL,'','',1,0,'F',0,0,1,'system:operlog:query','#','admin','2025-05-18 20:48:08','',NULL,''),(1041,'操作删除',500,2,'#',NULL,'','',1,0,'F',0,0,1,'system:operlog:remove','#','admin','2025-05-18 20:48:08','',NULL,''),(1042,'日志导出',500,4,'#',NULL,'','',1,0,'F',0,0,1,'system:operlog:export','#','admin','2025-05-18 20:48:08','',NULL,''),(1043,'登录查询',501,1,'#',NULL,'','',1,0,'F',0,0,1,'system:logininfor:query','#','admin','2025-05-18 20:48:08','',NULL,''),(1044,'登录删除',501,2,'#',NULL,'','',1,0,'F',0,0,1,'system:logininfor:remove','#','admin','2025-05-18 20:48:08','',NULL,''),(1045,'日志导出',501,3,'#',NULL,'','',1,0,'F',0,0,1,'system:logininfor:export','#','admin','2025-05-18 20:48:08','',NULL,''),(1046,'在线查询',109,1,'#',NULL,'','',1,0,'F',0,0,1,'monitor:online:query','#','admin','2025-05-18 20:48:08','',NULL,''),(1047,'批量强退',109,2,'#',NULL,'','',1,0,'F',0,0,1,'monitor:online:batchLogout','#','admin','2025-05-18 20:48:08','',NULL,''),(1048,'单条强退',109,3,'#',NULL,'','',1,0,'F',0,0,1,'monitor:online:forceLogout','#','admin','2025-05-18 20:48:08','',NULL,''),(1050,'账户解锁',501,4,'#',NULL,'','',1,0,'F',0,0,1,'system:logininfor:unlock','#','admin','2025-05-18 20:48:08','',NULL,''),(1055,'生成查询',115,1,'#',NULL,'','',1,0,'F',0,0,1,'tool:gen:query','#','admin','2025-05-18 20:48:08','',NULL,''),(1056,'生成修改',115,2,'#',NULL,'','',1,0,'F',0,0,1,'tool:gen:edit','#','admin','2025-05-18 20:48:08','',NULL,''),(1057,'生成删除',115,3,'#',NULL,'','',1,0,'F',0,0,1,'tool:gen:remove','#','admin','2025-05-18 20:48:08','',NULL,''),(1058,'导入代码',115,2,'#',NULL,'','',1,0,'F',0,0,1,'tool:gen:import','#','admin','2025-05-18 20:48:08','',NULL,''),(1059,'预览代码',115,4,'#',NULL,'','',1,0,'F',0,0,1,'tool:gen:preview','#','admin','2025-05-18 20:48:08','',NULL,''),(1060,'生成代码',115,5,'#',NULL,'','',1,0,'F',0,0,1,'tool:gen:code','#','admin','2025-05-18 20:48:08','',NULL,''),(1600,'文件查询',118,1,'#',NULL,'','',1,0,'F',0,0,1,'system:oss:query','#','admin','2025-05-18 20:48:08','',NULL,''),(1601,'文件上传',118,2,'#',NULL,'','',1,0,'F',0,0,1,'system:oss:upload','#','admin','2025-05-18 20:48:08','',NULL,''),(1602,'文件下载',118,3,'#',NULL,'','',1,0,'F',0,0,1,'system:oss:download','#','admin','2025-05-18 20:48:08','',NULL,''),(1603,'文件删除',118,4,'#',NULL,'','',1,0,'F',0,0,1,'system:oss:remove','#','admin','2025-05-18 20:48:08','',NULL,''),(1604,'配置添加',118,5,'#',NULL,'','',1,0,'F',0,0,1,'system:oss:add','#','admin','2025-05-18 20:48:08','',NULL,''),(1605,'配置编辑',118,6,'#',NULL,'','',1,0,'F',0,0,1,'system:oss:edit','#','admin','2025-05-18 20:48:08','',NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `id` bigint NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` tinyint(1) DEFAULT '0' COMMENT '公告状态（0:正常 1:关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'温馨提醒：2018-07-01 版本发布啦','2',_binary '新版本内容',0,'admin','2025-05-21 21:44:07','',NULL,'管理员'),(2,'维护通知：2018-07-01 系统凌晨维护','1',_binary '维护内容',0,'admin','2025-05-21 21:44:07','',NULL,'管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_operate_log`
--

DROP TABLE IF EXISTS `sys_operate_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_operate_log` (
  `id` bigint NOT NULL COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` tinyint DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` tinyint(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `operator_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `operate_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `operate_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `operate_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `operate_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` tinyint(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间(毫秒)',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_operate_log`
--

LOCK TABLES `sys_operate_log` WRITE;
/*!40000 ALTER TABLE `sys_operate_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_operate_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss`
--

DROP TABLE IF EXISTS `sys_oss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss` (
  `id` bigint NOT NULL COMMENT '对象存储主键',
  `file_name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
  `original_name` varchar(255) NOT NULL DEFAULT '' COMMENT '原名',
  `file_suffix` varchar(10) NOT NULL DEFAULT '' COMMENT '文件后缀名',
  `url` varchar(500) NOT NULL COMMENT 'URL地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '上传人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `service` varchar(20) NOT NULL DEFAULT 'minio' COMMENT '服务商',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='OSS对象存储';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss`
--

LOCK TABLES `sys_oss` WRITE;
/*!40000 ALTER TABLE `sys_oss` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oss` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss_config`
--

DROP TABLE IF EXISTS `sys_oss_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss_config` (
  `oss_config_id` bigint NOT NULL COMMENT '主建',
  `config_key` varchar(20) NOT NULL DEFAULT '' COMMENT '配置key',
  `access_key` varchar(255) DEFAULT '' COMMENT 'accessKey',
  `secret_key` varchar(255) DEFAULT '' COMMENT '秘钥',
  `bucket_name` varchar(255) DEFAULT '' COMMENT '桶名称',
  `prefix` varchar(255) DEFAULT '' COMMENT '前缀',
  `endpoint` varchar(255) DEFAULT '' COMMENT '访问站点',
  `domain` varchar(255) DEFAULT '' COMMENT '自定义域名',
  `is_https` char(1) DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
  `region` varchar(255) DEFAULT '' COMMENT '域',
  `access_policy` char(1) NOT NULL DEFAULT '1' COMMENT '桶权限类型(0=private 1=public 2=custom)',
  `status` tinyint(1) DEFAULT '1' COMMENT '是否默认（0=是,1=否）',
  `ext1` varchar(255) DEFAULT '' COMMENT '扩展字段',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oss_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对象存储配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss_config`
--

LOCK TABLES `sys_oss_config` WRITE;
/*!40000 ALTER TABLE `sys_oss_config` DISABLE KEYS */;
INSERT INTO `sys_oss_config` VALUES (1,'minio','ruoyi','ruoyi123','ruoyi','','127.0.0.1:9000','','N','','1',0,'','admin','2025-05-11 20:13:44','admin','2025-05-11 20:13:44',NULL),(2,'qiniu','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','s3-cn-north-1.qiniucs.com','','N','','1',1,'','admin','2025-05-11 20:13:44','admin','2025-05-11 20:13:44',NULL),(3,'aliyun','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','oss-cn-beijing.aliyuncs.com','','N','','1',1,'','admin','2025-05-11 20:13:44','admin','2025-05-11 20:13:44',NULL),(4,'qcloud','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi-1250000000','','cos.ap-beijing.myqcloud.com','','N','ap-beijing','1',1,'','admin','2025-05-11 20:13:44','admin','2025-05-11 20:13:44',NULL),(5,'image','ruoyi','ruoyi123','ruoyi','image','127.0.0.1:9000','','N','','1',1,'','admin','2025-05-11 20:13:44','admin','2025-05-11 20:13:44',NULL);
/*!40000 ALTER TABLE `sys_oss_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `id` bigint NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` tinyint(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0:存在 1:删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','董事长',1,0,0,'admin','2025-05-21 21:44:01','',NULL,''),(2,'se','项目经理',2,0,0,'admin','2025-05-21 21:44:01','',NULL,''),(3,'hr','人力资源',3,0,0,'admin','2025-05-21 21:44:01','',NULL,''),(4,'sysUser','普通员工',4,0,0,'admin','2025-05-21 21:44:01','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志（0:存在 1:删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,0,0,'admin','2025-05-21 21:44:01','',NULL,'超级管理员'),(2,'普通角色','common',2,'2',1,1,0,0,'admin','2025-05-21 21:44:01','',NULL,'普通角色');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,500),(2,501),(2,1000),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_code` varchar(30) NOT NULL COMMENT '用户账号',
  `user_name` varchar(30) NOT NULL COMMENT '用户名称',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(10) DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phone_no` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `pass_word` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0:正常 1:停用）',
  `deleted` char(1) DEFAULT '0' COMMENT '删除标志（0:存在 1:删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_user_code` (`user_code`),
  UNIQUE KEY `uk_sys_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','管理员','','sys_user','admin@163.com','15888888888','1','','$2a$10$xqt5fv4/gbvlCFbv/QSMruOzOljPeRD/gc47WL6QcGOcqYZARZyTa','0','0','127.0.0.1','2025-05-18 20:39:39','admin','2025-05-18 20:39:39','',NULL,'管理员'),(2,105,'levi','里维','','sys_user','levi@163.com','15666666666','1','','$2a$10$xqt5fv4/gbvlCFbv/QSMruOzOljPeRD/gc47WL6QcGOcqYZARZyTa','0','0','127.0.0.1','2025-05-18 20:39:39','admin','2025-05-18 20:39:39','',NULL,'测试员');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AT transaction mode undo table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-05 13:52:01
