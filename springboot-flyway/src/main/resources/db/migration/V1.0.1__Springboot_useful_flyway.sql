CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL COMMENT '登陆账号',
  `nickname` varchar(30) NOT NULL COMMENT '用于显示的名称',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `mobile` varchar(15) DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `headUrl` varchar(100) DEFAULT NULL COMMENT '用户头像',
  `gender` smallint(1) NOT NULL DEFAULT '0' COMMENT '性别 1男 2女 0未知',
  `status` int(3) DEFAULT '1' COMMENT '状态',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_un_username` (`username`),
  UNIQUE KEY `idx_un_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';


INSERT INTO `sys_user` (`id`, `username`, `nickname`, `password`, `mobile`, `email`, `headUrl`, `gender`, `status`, `create_at`)
VALUES
	(1, 'admin', '超级管理员', '123456', '13999999999', 'liuqi_0725@aliyun.com', NULL, 0, 1, NOW());
