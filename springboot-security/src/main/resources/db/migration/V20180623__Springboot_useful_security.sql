--
-- 角色表
--
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `vcid` varchar(20) DEFAULT '0' COMMENT '虚拟中心 ,默认 0 顶级',
  `name` varchar(50) DEFAULT NULL COMMENT '角色名',
  `un_key` varchar(50) DEFAULT NULL COMMENT '角色唯一 key',
  `status` int(3) DEFAULT '1' COMMENT '状态',
  `edit` int(1) DEFAULT '1' COMMENT '可编辑 0 否 1 是',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_un_role_key` (`un_key`),
  KEY `idx_role_query` (`vcid`,`edit`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

--
-- 权限表
--
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '权限名',
  `un_key` varchar(50) NOT NULL COMMENT '权限唯一 key',
  `pid` int(11) DEFAULT '0' COMMENT '父级 ID 顶级为0',
  `nodepath` varchar(100) NOT NULL DEFAULT '权限层级，排序',
  `url` varchar(200) DEFAULT NULL COMMENT '权限 URL',
  `default_has` int(1) DEFAULT '0' COMMENT '是否所有用户默认权限 0 否 1 是',
  `status` int(3) DEFAULT '1' COMMENT '状态',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_un_permission_key` (`un_key`),
  KEY `idx_permission_query` (`status`,`default_has`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限';

--
-- 角色权限关联表
--
CREATE TABLE `sys_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) NOT NULL COMMENT '角色 ID',
  `pid` int(11) NOT NULL COMMENT '权限 ID',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_permission_query` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';

--
-- 用户表
--
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vcid` varchar(20) DEFAULT '0' COMMENT '虚拟中心 ,默认 0',
  `username` varchar(45) NOT NULL COMMENT '登陆账号',
  `nickname` varchar(30) NOT NULL COMMENT '用于显示的名称',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `mobile` varchar(15) DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `headUrl` varchar(100) DEFAULT NULL COMMENT '用户头像',
  `gender` smallint(1) NOT NULL DEFAULT '0' COMMENT '性别 1男 2女 0未知',
  `status` int(3) DEFAULT '1' COMMENT '状态',
  `edit` int(1) DEFAULT '1' COMMENT '可编辑 0 否 1 是',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_un_username` (`username`),
  UNIQUE KEY `idx_un_mobile` (`mobile`),
  KEY `idx_user_query` (`username`,`vcid`,`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

--
-- 用户角色关联表
--
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL COMMENT '用户 ID',
  `rid` int(11) NOT NULL COMMENT '角色 ID',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_role_query` (`uid`,`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

--
-- 菜单表
--
CREATE TABLE `sys_menus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '菜单名称',
  `un_key` varchar(50) NOT NULL COMMENT '菜单 KEY',
  `pid` int(11) DEFAULT '0' COMMENT '父级 ID 顶级为0',
  `nodepath` varchar(100) NOT NULL DEFAULT '层级，排序',
  `url` varchar(200) DEFAULT NULL COMMENT '点击URL',
  `icon` varchar(200) DEFAULT NULL COMMENT '菜单 icon class',
  `status` int(3) DEFAULT '1' COMMENT '状态',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_un_menus_key` (`un_key`),
  KEY `idx_menus_query` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';


--
-- 插入角色测试数据
--
INSERT INTO `sys_role` (`id`, `vcid`, `name`, `un_key`, `status`, `edit`, `create_at`)
VALUES
	(1, '0', '超级管理员', 'SUPER_ADMIN', 1, 0, NOW()),
	(2, '0', '角色1', 'ROLE-1', 1, 1, NOW()),
	(3, '0', '角色2', 'ROLE-2', 1, 1, NOW());

--
-- 插入权限测试数据
--
INSERT INTO `sys_permission` (`id`, `name`, `un_key`, `pid`, `nodepath`, `url`)
VALUES
	(1, '首页', 'P_DASHBOARD', 0, '001', '/dashboard' ),
	(2, '用户相关', 'P_USER', 0, '002', '/user/**' ),
	(3, '查看源码', 'P_SOURCE', 0, '003', '/where/**'),
    (4, '查看授权', 'P_AUTH', 0, '004', '/auth/**');


--
-- 插入角色权限测试数据
--
INSERT INTO `sys_role_permission` (`rid`, `pid`)
VALUES
	(2 , 1),
	(2 , 2),
	(2 , 3),
	(2 , 4),
	(3 , 1),
	(3 , 2),
	(3 , 3);

--
-- 插入用户测试数据
--
INSERT INTO `sys_user` (`id`, `vcid`, `username`, `nickname`, `password`, `mobile`, `email`, `gender`, `edit`, `create_at`)
VALUES
	(1, '0', 'admin', '超级管理员', '$2a$10$AYKt1//xlSezfkiZoAUVneszxzFt04I2vBIouYI.WDchTWbvnmhWy', '13987654321', 'liuqi_0725@aliyun.com', 1, 0, NOW()),
	(2, '0', 'test1', 'test1', '$2a$10$AE0nZCJCcG8mTrC3IOASz.KhqOeeB6ypOhbyaZrk4uj14P6XFVohS', '13987654322', '', 1, 1, NOW()),
	(3, '0', 'test2', 'test2', '$2a$10$AE0nZCJCcG8mTrC3IOASz.KhqOeeB6ypOhbyaZrk4uj14P6XFVohS', '13987654323', '', 1, 1, NOW()),
	(4, '0', 'test3', 'test3', '$2a$10$AE0nZCJCcG8mTrC3IOASz.KhqOeeB6ypOhbyaZrk4uj14P6XFVohS', '13987654324', '', 2, 1, NOW());

--
-- 插入用户角色测试数据
--
INSERT INTO `sys_user_role` (`uid`, `rid`)
VALUES
	(1, 1),
	(2, 2),
	(3, 2),
	(4, 3);

--
-- 插入菜单测试数据
--
INSERT INTO `sys_menus` (`id`, `name`, `un_key`, `pid`, `nodepath`, `url`, `icon`, `status`)
VALUES
	(1, '我的主页', 'M_DASHBOARD', 0, '001', '/dashboard', '', 1),
	(2, '用户管理', 'M_USER_MANAGE', 0, '002', '/user/list', '', 1),
	(3, '用户信息', 'M_USER_INFO', 0, '003', '/user/info', '', 1),
	(4, '源码地址', 'M_SOURCE', 0, '004', '', '', 1),
	(5, '授权信息', 'M_AUTH', 0, '005', '', '', 1),
	(6, 'Github 地址', 'M_SOURCE_GITHUB', 4, '00401', '/where/github', '', 1),
	(7, 'Gitee 地址', 'M_SOURCE_GITEE', 4, '00402', '/where/gitee', '', 1),
	(8, '权限信息', 'M_AUTH_PERMISSION', 5, '00501', '/auth/perm-list', '', 1),
	(9, '菜单信息',  'M_MENU_INFO', 5, '00502', '/auth/menu-list', '', 1),
	(10, '角色信息',  'M_ROLE_INFO', 5, '00503', '/auth/role-info', '', 1);