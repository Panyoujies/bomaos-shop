/*
 Navicat MySQL Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost
 Source Database       : zlianweb-demo

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : utf-8

 Date: 10/06/2022 07:32:31 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `oauth_token`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_token`;
CREATE TABLE `oauth_token` (
  `token_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) NOT NULL,
  `access_token` varchar(128) NOT NULL,
  `refresh_token` varchar(128) DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  `refresh_token_expire_time` datetime DEFAULT NULL,
  `roles` varchar(512) DEFAULT NULL,
  `permissions` varchar(512) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `oauth_token_key`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_token_key`;
CREATE TABLE `oauth_token_key` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token_key` varchar(128) NOT NULL COMMENT '生成token时的key',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `sys_article`
-- ----------------------------
DROP TABLE IF EXISTS `sys_article`;
CREATE TABLE `sys_article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '文章标题',
  `excerpt` longtext COMMENT '摘要',
  `content` longtext COMMENT '文章内容',
  `likes` int DEFAULT NULL COMMENT '点赞数',
  `see_number` int DEFAULT NULL COMMENT '查看数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` int DEFAULT NULL COMMENT '用户uid',
  `picture` varchar(255) DEFAULT NULL COMMENT '文章图片',
  `enabled` int DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- ----------------------------
--  Table structure for `sys_cards`
-- ----------------------------
DROP TABLE IF EXISTS `sys_cards`;
CREATE TABLE `sys_cards` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_id` int DEFAULT NULL COMMENT '对应商品id',
  `card_info` text COMMENT '卡密',
  `status` int DEFAULT NULL COMMENT '卡密状态',
  `sell_type` int DEFAULT NULL COMMENT '售卡类型',
  `number` int DEFAULT NULL COMMENT '总数',
  `sell_number` int DEFAULT NULL COMMENT '售出数量',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=utf8mb4 COMMENT='卡密';

-- ----------------------------
--  Table structure for `sys_carousel`
-- ----------------------------
DROP TABLE IF EXISTS `sys_carousel`;
CREATE TABLE `sys_carousel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `link` longtext COMMENT '对应的链接地址',
  `image_logo` longtext COMMENT '图片地址',
  `summary` longtext COMMENT '简介',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `enabled` int DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='轮播图管理';

-- ----------------------------
--  Records of `sys_carousel`
-- ----------------------------
BEGIN;
INSERT INTO `sys_carousel` VALUES ('1', '值联云卡 Lite 版', 'https://free.zlianpay.cn', '/file/20220325/6072308b8b4924ff779ab83c608a5f96.jpeg', '点击进入值联官网', '2022-03-23 17:26:57', '2022-03-23 17:26:57', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_classifys`
-- ----------------------------
DROP TABLE IF EXISTS `sys_classifys`;
CREATE TABLE `sys_classifys` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(200) DEFAULT NULL COMMENT '分类名称',
  `status` int DEFAULT NULL COMMENT '分类状态',
  `sort` int DEFAULT NULL COMMENT '排序',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='分类';

-- ----------------------------
--  Records of `sys_classifys`
-- ----------------------------
BEGIN;
INSERT INTO `sys_classifys` VALUES ('1', '官方分类', '1', '1000', '2022-03-23 02:20:47', '2022-03-23 02:20:47'), ('2', '测试分类', '0', '1000', '2022-03-23 20:18:03', '2022-03-23 20:18:03');
COMMIT;

-- ----------------------------
--  Table structure for `sys_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `sys_coupon`;
CREATE TABLE `sys_coupon` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `classifys_id` int DEFAULT NULL COMMENT '分类id',
  `product_id` int DEFAULT NULL COMMENT '商品id',
  `type` int DEFAULT NULL COMMENT '类型-0一次性，1重复使用',
  `status` int DEFAULT NULL COMMENT '状态',
  `coupon` varchar(255) DEFAULT NULL COMMENT '优惠券代码',
  `discount_type` int DEFAULT NULL COMMENT '面额或者百分比',
  `discount_val` decimal(18,2) DEFAULT NULL COMMENT '面额、折扣 价格和百分比',
  `count_used` int DEFAULT NULL COMMENT '已使用次数',
  `count_all` int DEFAULT NULL COMMENT '可用次数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `full_reduction` decimal(18,2) DEFAULT NULL COMMENT '满减金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券';

-- ----------------------------
--  Table structure for `sys_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
  `dict_id` int NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `dict_code` varchar(100) NOT NULL COMMENT '字典标识',
  `dict_name` varchar(200) NOT NULL COMMENT '字典名称',
  `sort_number` int NOT NULL DEFAULT '1' COMMENT '排序号',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='字典';

-- ----------------------------
--  Records of `sys_dictionary`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dictionary` VALUES ('1', 'sex', '性别', '1', '', '0', '2020-03-15 13:04:39', '2020-03-15 13:04:39'), ('2', 'organization_type', '机构类型', '2', '', '0', '2020-03-16 00:32:36', '2020-03-16 00:32:36');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dictionary_data`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary_data`;
CREATE TABLE `sys_dictionary_data` (
  `dict_data_id` int NOT NULL AUTO_INCREMENT COMMENT '字典项id',
  `dict_id` int NOT NULL COMMENT '字典id',
  `dict_data_code` varchar(100) NOT NULL COMMENT '字典项标识',
  `dict_data_name` varchar(200) NOT NULL COMMENT '字典项名称',
  `sort_number` int NOT NULL DEFAULT '1' COMMENT '排序号',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_data_id`),
  KEY `dict_id` (`dict_id`),
  CONSTRAINT `sys_dictionary_data_ibfk_1` FOREIGN KEY (`dict_id`) REFERENCES `sys_dictionary` (`dict_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='字典项';

-- ----------------------------
--  Records of `sys_dictionary_data`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dictionary_data` VALUES ('1', '1', 'male', '男', '1', '', '0', '2020-03-15 13:07:28', '2020-03-15 13:07:28'), ('2', '1', 'female', '女', '2', '', '0', '2020-03-15 13:07:41', '2020-03-15 15:58:04'), ('3', '2', 'company', '公司', '1', '', '0', '2020-03-16 00:34:32', '2020-03-16 00:34:32'), ('4', '2', 'subsidiary', '子公司', '2', '', '0', '2020-03-16 00:35:02', '2020-03-16 00:35:02'), ('5', '2', 'department', '部门', '3', '', '0', '2020-03-16 00:35:18', '2020-03-16 00:35:18'), ('6', '2', 'group', '小组', '4', '', '0', '2020-03-16 00:35:36', '2020-03-16 00:35:36');
COMMIT;

-- ----------------------------
--  Table structure for `sys_links`
-- ----------------------------
DROP TABLE IF EXISTS `sys_links`;
CREATE TABLE `sys_links` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `link` longtext COMMENT '链接地址',
  `enabled` int DEFAULT NULL COMMENT '是否开启',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remote_enabled` int DEFAULT NULL COMMENT '远程端',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='友情链接表';

-- ----------------------------
--  Table structure for `sys_login_record`
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_record`;
CREATE TABLE `sys_login_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) NOT NULL COMMENT '用户账号',
  `os` varchar(200) DEFAULT NULL COMMENT '操作系统',
  `device` varchar(200) DEFAULT NULL COMMENT '设备名',
  `browser` varchar(200) DEFAULT NULL COMMENT '浏览器类型',
  `ip` varchar(200) DEFAULT NULL COMMENT 'ip地址',
  `oper_type` int NOT NULL COMMENT '操作类型,0登录成功,1登录失败,2退出登录,3刷新token',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='登录日志';

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` int NOT NULL DEFAULT '0' COMMENT '上级id,0是顶级',
  `menu_name` varchar(200) NOT NULL COMMENT '菜单名称',
  `menu_icon` varchar(200) DEFAULT NULL COMMENT '菜单图标',
  `path` varchar(200) DEFAULT NULL COMMENT '菜单地址',
  `menu_type` int DEFAULT '0' COMMENT '类型,0菜单,1按钮',
  `sort_number` int NOT NULL DEFAULT '1' COMMENT '排序号',
  `authority` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `target` varchar(200) DEFAULT '_self' COMMENT '打开位置',
  `icon_color` varchar(200) DEFAULT NULL COMMENT '图标颜色',
  `hide` int NOT NULL DEFAULT '0' COMMENT '是否隐藏,0否,1是',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COMMENT='菜单';

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', 'layui-icon layui-icon-set-sm', '', '0', '997', '', '_self', null, '0', '0', '2020-02-26 12:51:23', '2021-03-27 20:25:24'), ('2', '1', '用户管理', null, 'sys/user', '0', '1', 'sys:user:view', '_self', null, '0', '0', '2020-02-26 12:51:55', '2020-03-21 18:45:26'), ('3', '2', '查询用户', null, null, '1', '1', 'sys:user:list', '_self', null, '0', '0', '2020-02-26 12:52:06', '2020-03-21 18:45:28'), ('4', '2', '添加用户', null, null, '1', '2', 'sys:user:save', '_self', null, '0', '0', '2020-02-26 12:52:26', '2020-03-21 18:45:29'), ('5', '2', '修改用户', null, null, '1', '3', 'sys:user:update', '_self', null, '0', '0', '2020-02-26 12:52:50', '2020-03-21 18:45:30'), ('6', '2', '删除用户', null, null, '1', '4', 'sys:user:remove', '_self', null, '0', '0', '2020-02-26 12:53:13', '2020-03-21 18:45:32'), ('7', '1', '角色管理', null, 'sys/role', '0', '2', 'sys:role:view', '_self', null, '0', '0', '2020-03-13 13:29:08', '2020-03-21 18:45:33'), ('8', '7', '查询角色', null, null, '1', '1', 'sys:role:list', '_self', null, '0', '0', '2020-03-13 13:30:41', '2020-03-21 18:45:34'), ('9', '7', '添加角色', null, null, '1', '2', 'sys:role:save', '_self', null, '0', '0', '2020-03-15 13:02:07', '2020-03-21 18:45:35'), ('10', '7', '修改角色', null, null, '1', '3', 'sys:role:update', '_self', null, '0', '0', '2020-03-15 13:02:49', '2020-03-21 18:45:36'), ('11', '7', '删除角色', null, null, '1', '4', 'sys:role:remove', '_self', null, '0', '0', '2020-03-20 17:58:51', '2020-03-21 18:45:38'), ('12', '1', '菜单管理', null, 'sys/menu', '0', '3', 'sys:menu:view', '_self', null, '0', '0', '2020-03-21 01:07:13', '2020-03-21 18:45:39'), ('13', '12', '查询菜单', null, null, '1', '1', 'sys:menu:list', '_self', null, '0', '0', '2020-03-21 16:43:30', '2020-03-21 18:45:40'), ('14', '12', '添加菜单', null, null, '1', '2', 'sys:menu:save', '_self', null, '0', '0', '2020-03-21 16:43:54', '2020-03-21 18:45:41'), ('15', '12', '修改菜单', null, null, '1', '3', 'sys:menu:update', '_self', null, '0', '0', '2020-03-21 18:24:17', '2020-03-21 18:45:43'), ('16', '12', '删除菜单', null, null, '1', '4', 'sys:menu:remove', '_self', null, '0', '0', '2020-03-21 18:24:18', '2020-03-21 18:45:44'), ('17', '1', '机构管理', '', 'sys/organization', '0', '4', 'sys:org:view', '_self', null, '1', '0', '2020-03-21 18:24:20', '2021-11-10 16:41:09'), ('18', '17', '查询机构', null, null, '1', '1', 'sys:org:list', '_self', null, '0', '0', '2020-03-21 18:24:21', '2020-03-21 18:44:36'), ('19', '17', '添加机构', null, null, '1', '2', 'sys:org:save', '_self', null, '0', '0', '2020-03-21 18:24:22', '2020-03-21 18:45:51'), ('20', '17', '修改机构', null, null, '1', '3', 'sys:org:update', '_self', null, '0', '0', '2020-03-21 18:24:24', '2020-03-21 18:45:52'), ('21', '17', '删除机构', null, null, '1', '4', 'sys:org:remove', '_self', null, '0', '0', '2020-03-21 18:24:25', '2020-03-21 18:45:54'), ('22', '1', '字典管理', '', 'sys/dict', '0', '5', 'sys:dict:view', '_self', null, '1', '0', '2020-03-21 18:24:26', '2021-11-10 16:41:17'), ('23', '22', '查询字典', null, null, '1', '1', 'sys:dict:list', '_self', null, '0', '0', '2020-03-21 18:24:27', '2020-03-21 18:44:42'), ('24', '22', '添加字典', null, null, '1', '2', 'sys:dict:save', '_self', null, '0', '0', '2020-03-21 18:24:28', '2020-03-21 18:45:59'), ('25', '22', '修改字典', null, null, '1', '3', 'sys:dict:update', '_self', null, '0', '0', '2020-03-21 18:24:29', '2020-03-21 18:46:01'), ('26', '22', '删除字典', null, null, '1', '4', 'sys:dict:remove', '_self', null, '0', '0', '2020-03-21 18:24:31', '2020-03-21 18:46:02'), ('27', '0', '日志管理', 'layui-icon layui-icon-list', '', '0', '998', '', '_self', null, '0', '0', '2020-03-21 18:24:32', '2021-03-27 20:25:31'), ('28', '27', '登录日志', null, 'sys/loginRecord', '0', '1', 'sys:login_record:view', '_self', null, '0', '0', '2020-03-21 18:24:33', '2020-03-21 18:44:52'), ('29', '27', '操作日志', null, 'sys/operRecord', '0', '2', 'sys:oper_record:view', '_self', null, '0', '0', '2020-03-21 18:24:34', '2020-03-21 18:46:10'), ('30', '27', '数据监控', null, 'druid', '0', '3', null, '_self', null, '0', '0', '2020-03-21 18:24:35', '2020-03-22 14:46:21'), ('31', '0', '系统工具', 'layui-icon layui-icon-slider', '', '0', '999', '', '_self', null, '0', '0', '2020-03-21 18:24:36', '2021-03-27 20:25:39'), ('32', '31', '文件管理', null, 'file/manage', '0', '1', 'sys:file:view', '_self', null, '0', '0', '2020-03-21 18:24:38', '2020-03-22 14:46:57'), ('33', '32', '查询文件', null, null, '1', '1', 'sys:file:list', '_self', null, '0', '0', '2020-03-21 18:24:39', '2020-03-22 14:47:32'), ('34', '32', '删除文件', null, null, '1', '2', 'sys:file:remove', '_self', null, '0', '0', '2020-03-21 18:24:40', '2020-03-22 14:46:54'), ('35', '31', '发送邮件', null, 'sys/email', '0', '2', 'sys:email:view', '_self', null, '0', '0', '2020-03-21 18:24:41', '2020-03-22 14:47:35'), ('36', '31', '项目生成', '', '', '0', '3', '', '_self', null, '1', '1', '2020-03-21 18:24:42', '2021-11-10 16:41:57'), ('37', '0', '商品管理', 'layui-icon layui-icon-cart-simple', '', '0', '2', '', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-30 02:17:40'), ('38', '37', '分类管理', '', 'products/classifys', '0', '1', 'products:classifys:view', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:26:54'), ('39', '38', '查询分类', '', '', '1', '1', 'products:classifys:list', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('40', '38', '添加分类', '', '', '1', '2', 'products:classifys:save', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('41', '38', '修改分类', '', '', '1', '3', 'products:classifys:update', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('42', '38', '删除分类', '', '', '1', '4', 'products:classifys:remove', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('43', '37', '商品管理', '', 'products/products', '0', '3', 'products:products:view', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-06-05 15:51:44'), ('44', '43', '查询商品', '', '', '1', '1', 'products:products:list', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('45', '43', '添加商品', '', '', '1', '2', 'products:products:save', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('46', '43', '修改商品', '', '', '1', '3', 'products:products:update', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('47', '43', '删除商品', '', '', '1', '4', 'products:products:remove', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20'), ('48', '0', '卡密管理', 'layui-icon layui-icon-template-1', '', '0', '3', '', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-30 02:18:38'), ('49', '48', '卡密管理', '', 'carmi/cards', '0', '2', 'carmi:cards:view', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:46:41'), ('50', '49', '查询卡密', '', '', '1', '1', 'carmi:cards:list', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:34:17'), ('51', '54', '添加卡密', '', '', '1', '2', 'carmi:cards:save', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:46:54'), ('52', '49', '修改卡密', '', '', '1', '3', 'carmi:cards:update', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:34:17'), ('53', '49', '删除卡密', '', '', '1', '4', 'carmi:cards:remove', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:34:17'), ('54', '48', '添加卡密', '', 'carmi/cards/add', '0', '1', 'carmi:cards:view', '_self', null, '0', '0', '2021-03-28 00:46:11', '2021-03-28 00:46:26'), ('55', '48', '售出卡密', '', 'carmi/cards/sold', '0', '3', 'carmi:cards:view', '_self', null, '0', '1', '2021-03-28 14:36:53', '2021-11-02 15:12:47'), ('56', '43', '查询卡密', '', '', '1', '1', 'carmi:cards:list', '_self', null, '0', '0', '2021-03-28 14:37:26', '2021-11-10 03:03:13'), ('57', '0', '系统配置', 'layui-icon layui-icon-set-fill', '', '0', '996', '', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-11-10 16:40:55'), ('58', '57', '支付配置', '', 'settings/pays', '0', '2', 'settings:pays:view', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-07-04 04:01:04'), ('59', '58', '查询支付配置', '', '', '1', '1', 'settings:pays:list', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27'), ('60', '58', '添加支付配置', '', '', '1', '2', 'settings:pays:save', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27'), ('61', '58', '修改支付配置', '', '', '1', '3', 'settings:pays:update', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27'), ('62', '58', '删除支付配置', '', '', '1', '4', 'settings:pays:remove', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27'), ('63', '0', '订单管理', 'layui-icon layui-icon-rmb', '', '0', '4', '', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-30 02:18:55'), ('64', '63', '订单流水', '', 'orders/orders', '0', '1', 'orders:orders:view', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 17:01:42'), ('65', '64', '查询订单表', '', '', '1', '1', 'orders:orders:list', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07'), ('66', '64', '添加订单表', '', '', '1', '2', 'orders:orders:save', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07'), ('67', '64', '修改订单表', '', '', '1', '3', 'orders:orders:update', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07'), ('68', '64', '删除订单表', '', '', '1', '4', 'orders:orders:remove', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07'), ('69', '0', '卡密订单关联', 'layui-icon layui-icon-senior', '', '0', '999', 'carmi:orderCard:view', '_self', null, '1', '1', '2021-03-29 22:28:45', '2021-11-02 16:57:33'), ('70', '69', '订单关联卡密表管理', '', 'carmi/orderCard', '0', '1', 'carmi:orderCard:view', '_self', null, '0', '1', '2021-03-29 22:28:45', '2021-11-02 16:57:29'), ('71', '70', '查询订单关联卡密表', '', '', '1', '1', 'carmi:orderCard:list', '_self', null, '0', '1', '2021-03-29 22:28:45', '2021-11-02 16:57:56'), ('72', '70', '添加订单关联卡密表', '', '', '1', '2', 'carmi:orderCard:save', '_self', null, '0', '1', '2021-03-29 22:28:45', '2021-11-02 16:57:51'), ('73', '70', '修改订单关联卡密表', '', '', '1', '3', 'carmi:orderCard:update', '_self', null, '0', '1', '2021-03-29 22:28:45', '2021-11-02 16:58:02'), ('74', '70', '删除订单关联卡密表', '', '', '1', '4', 'carmi:orderCard:remove', '_self', null, '0', '1', '2021-03-29 22:28:45', '2021-11-02 16:58:05'), ('75', '0', '管理中心', 'layui-icon layui-icon-console', 'dashboard/workplace', '0', '1', 'dashboard:user:view', '_self', null, '0', '0', '2021-03-30 00:58:26', '2021-03-30 02:16:33'), ('76', '37', '添加商品', '', 'products/products/addProduct', '0', '2', 'products:products:view', '_self', null, '0', '0', '2021-06-05 15:51:32', '2021-06-05 15:52:52'), ('77', '0', 'website', 'layui-icon layui-icon-senior', '', '0', '1', '', '_self', null, '0', '1', '2021-06-06 02:17:34', '2021-06-06 02:19:37'), ('78', '57', '网站设置', '', 'website/website', '0', '4', 'website:website:view', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-07-04 04:01:28'), ('79', '78', '查询网站设置', '', '', '1', '1', 'website:website:list', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34'), ('80', '78', '添加网站设置', '', '', '1', '2', 'website:website:save', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34'), ('81', '78', '修改网站设置', '', '', '1', '3', 'website:website:update', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34'), ('82', '78', '删除网站设置', '', '', '1', '4', 'website:website:remove', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34'), ('83', '0', '营销助手', 'layui-icon layui-icon-senior', '', '0', '5', '', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:54:20'), ('84', '83', '优惠券管理', '', 'settings/coupon', '0', '2', 'settings:coupon:view', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:55:51'), ('85', '84', '查询优惠券', '', '', '1', '1', 'settings:coupon:list', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46'), ('86', '84', '添加优惠券', '', '', '1', '2', 'settings:coupon:save', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46'), ('87', '84', '修改优惠券', '', '', '1', '3', 'settings:coupon:update', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46'), ('88', '84', '删除优惠券', '', '', '1', '4', 'settings:coupon:remove', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46'), ('89', '83', '添加优惠券', '', 'settings/coupon/add', '0', '1', 'settings:coupon:view', '_self', null, '0', '0', '2021-06-23 07:55:44', '2021-06-23 07:56:13'), ('90', '0', 'theme', 'layui-icon layui-icon-senior', '', '0', '1', '', '_self', null, '0', '1', '2021-06-28 00:37:42', '2021-06-28 00:39:46'), ('91', '57', '主题配置', '', 'theme/theme', '0', '3', 'theme:theme:view', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:39:43'), ('92', '91', '查询主题配置', '', '', '1', '1', 'theme:theme:list', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42'), ('93', '91', '添加主题配置', '', '', '1', '2', 'theme:theme:save', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42'), ('94', '91', '修改主题配置', '', '', '1', '3', 'theme:theme:update', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42'), ('95', '91', '删除主题配置', '', '', '1', '4', 'theme:theme:remove', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42'), ('96', '0', 'settings', 'layui-icon layui-icon-senior', '', '0', '1', '', '_self', null, '0', '1', '2021-07-04 03:59:22', '2021-07-04 04:00:42'), ('97', '57', '商店设置', '', 'settings/shopSettings', '0', '1', 'settings:shopSettings:view', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 04:01:37'), ('98', '97', '查询商店设置', '', '', '1', '1', 'settings:shopSettings:list', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22'), ('99', '97', '添加商店设置', '', '', '1', '2', 'settings:shopSettings:save', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22'), ('100', '97', '修改商店设置', '', '', '1', '3', 'settings:shopSettings:update', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22'), ('101', '97', '删除商店设置', '', '', '1', '4', 'settings:shopSettings:remove', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22'), ('102', '57', '微信通知', '', 'wxpusher/send', '0', '2', 'settings:wxpusher:view', '_self', null, '0', '0', '2021-07-04 16:35:26', '2021-07-04 16:35:38'), ('103', '0', '内容管理', 'layui-icon layui-icon-senior', '', '0', '30', '', '_self', null, '0', '0', '2021-11-08 04:59:08', '2021-11-08 06:10:02'), ('104', '103', '文章管理', '', 'content/article', '0', '2', 'content:article:view', '_self', null, '0', '0', '2021-11-08 04:59:08', '2021-11-10 03:05:20'), ('105', '104', '查询文章表', '', '', '1', '1', 'content:article:list', '_self', null, '0', '0', '2021-11-08 04:59:08', '2021-11-08 04:59:08'), ('106', '104', '添加文章表', '', '', '1', '2', 'content:article:save', '_self', null, '0', '0', '2021-11-08 04:59:08', '2021-11-08 04:59:08'), ('107', '104', '修改文章表', '', '', '1', '3', 'content:article:update', '_self', null, '0', '0', '2021-11-08 04:59:08', '2021-11-08 04:59:08'), ('108', '104', '删除文章表', '', '', '1', '4', 'content:article:remove', '_self', null, '0', '0', '2021-11-08 04:59:08', '2021-11-08 04:59:08'), ('109', '103', '添加文章', '', 'content/article/addArticle', '0', '1', 'content:article:view', '_self', null, '0', '0', '2021-11-08 06:23:01', '2021-11-08 06:23:35'), ('110', '109', '添加文章表', '', '', '1', '1', 'content:article:save', '_self', null, '0', '0', '2021-11-08 06:24:24', '2021-11-08 06:25:03'), ('111', '0', 'content', 'layui-icon layui-icon-senior', '', '0', '1', '', '_self', null, '0', '1', '2021-11-10 03:01:51', '2021-11-10 03:03:49'), ('112', '103', '首页轮播', '', 'content/carousel', '0', '4', 'content:carousel:view', '_self', null, '0', '0', '2021-11-10 03:01:51', '2021-11-10 08:30:59'), ('113', '112', '查询轮播图管理', '', '', '1', '1', 'content:carousel:list', '_self', null, '0', '0', '2021-11-10 03:01:51', '2021-11-10 03:01:51'), ('114', '112', '添加轮播图管理', '', '', '1', '2', 'content:carousel:save', '_self', null, '0', '0', '2021-11-10 03:01:51', '2021-11-10 03:01:51'), ('115', '112', '修改轮播图管理', '', '', '1', '3', 'content:carousel:update', '_self', null, '0', '0', '2021-11-10 03:01:51', '2021-11-10 03:01:51'), ('116', '112', '删除轮播图管理', '', '', '1', '4', 'content:carousel:remove', '_self', null, '0', '0', '2021-11-10 03:01:51', '2021-11-10 03:01:51'), ('117', '103', '添加轮播图', '', 'content/carousel/addCarousel', '0', '3', 'content:carousel:view', '_self', null, '0', '1', '2021-11-10 03:05:12', '2021-11-10 08:30:37'), ('118', '117', '添加轮播图管理', '', '', '1', '1', 'content:carousel:save', '_self', null, '0', '0', '2021-11-10 03:06:39', '2021-11-10 03:06:39');
COMMIT;

-- ----------------------------
--  Table structure for `sys_oper_record`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_record`;
CREATE TABLE `sys_oper_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `model` varchar(200) DEFAULT NULL COMMENT '操作模块',
  `description` varchar(200) DEFAULT NULL COMMENT '操作方法',
  `url` varchar(200) DEFAULT NULL COMMENT '请求地址',
  `request_method` varchar(200) DEFAULT NULL COMMENT '请求方式',
  `oper_method` varchar(200) DEFAULT NULL COMMENT '调用方法',
  `param` varchar(2000) DEFAULT NULL COMMENT '请求参数',
  `result` varchar(2000) DEFAULT NULL COMMENT '返回结果',
  `ip` varchar(200) DEFAULT NULL COMMENT 'ip地址',
  `comments` varchar(2000) DEFAULT NULL COMMENT '备注',
  `spend_time` int DEFAULT NULL COMMENT '请求耗时,单位毫秒',
  `state` int NOT NULL DEFAULT '0' COMMENT '状态,0成功,1异常',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `sys_oper_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1017 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

-- ----------------------------
--  Table structure for `sys_orders`
-- ----------------------------
DROP TABLE IF EXISTS `sys_orders`;
CREATE TABLE `sys_orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `member` varchar(255) DEFAULT NULL COMMENT '订单号',
  `status` int DEFAULT NULL COMMENT '状态',
  `number` int DEFAULT NULL COMMENT '订单数量',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `contact` varchar(255) DEFAULT NULL COMMENT '买家联系方式',
  `product_id` int DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `ship_type` int DEFAULT NULL COMMENT '发货模式',
  `pay_type` varchar(255) DEFAULT NULL COMMENT '支付类型',
  `guest_id` int DEFAULT NULL COMMENT '支付用户的id（如果有）',
  `ip` varchar(255) DEFAULT NULL COMMENT '买家ip',
  `device` varchar(255) DEFAULT NULL COMMENT '购买设备',
  `pay_no` varchar(255) DEFAULT NULL COMMENT '流水号',
  `money` decimal(18,2) DEFAULT NULL COMMENT '付款金额',
  `price` decimal(18,2) NOT NULL COMMENT '提交金额',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `cloud_payid` varchar(255) DEFAULT '' COMMENT '云端id',
  `email` varchar(255) DEFAULT NULL COMMENT '邮件通知',
  `is_coupon` int DEFAULT NULL COMMENT '是否使用优惠券',
  `coupon_id` int DEFAULT NULL COMMENT '优惠券id',
  `user_id` int unsigned DEFAULT NULL COMMENT '用户id',
  `password` varchar(255) DEFAULT NULL COMMENT '订单密码',
  `cards_info` longtext COMMENT '卡密信息',
  `attach_info` longtext COMMENT '附加信息',
  `handling_fee` decimal(18,2) DEFAULT NULL COMMENT '手续费',
  PRIMARY KEY (`id`,`price`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
--  Table structure for `sys_organization`
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization` (
  `organization_id` int NOT NULL AUTO_INCREMENT COMMENT '机构id',
  `parent_id` int NOT NULL DEFAULT '0' COMMENT '上级id,0是顶级',
  `organization_name` varchar(200) NOT NULL COMMENT '机构名称',
  `organization_full_name` varchar(200) DEFAULT NULL COMMENT '机构全称',
  `organization_code` varchar(100) DEFAULT NULL COMMENT '机构代码',
  `organization_type` int NOT NULL COMMENT '机构类型',
  `leader_id` int DEFAULT NULL COMMENT '负责人id',
  `sort_number` int NOT NULL DEFAULT '1' COMMENT '排序号',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`organization_id`),
  KEY `leader_id` (`leader_id`),
  CONSTRAINT `sys_organization_ibfk_1` FOREIGN KEY (`leader_id`) REFERENCES `sys_user` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='组织机构';

-- ----------------------------
--  Records of `sys_organization`
-- ----------------------------
BEGIN;
INSERT INTO `sys_organization` VALUES ('1', '0', 'XXX公司', 'XXXXXXXXX科技有限公司', null, '3', null, '1', '', '0', '2020-03-15 13:14:55', '2020-03-21 15:12:49'), ('2', '1', '研发部', '研发部', null, '5', null, '2', '', '0', '2020-03-15 13:15:16', '2020-03-16 00:43:09'), ('3', '2', '高教组', '高等教育行业项目组', null, '6', null, '3', '', '0', '2020-03-15 13:15:45', '2020-03-16 00:42:49'), ('4', '2', '政务组', '政务行业项目组', null, '6', null, '4', '', '0', '2020-03-15 13:16:15', '2020-03-16 00:42:54'), ('5', '2', '制造组', '生产制造行业项目组', null, '6', null, '5', '', '0', '2020-03-15 13:16:37', '2020-03-21 15:13:05'), ('6', '2', '仿真组', '虚拟仿真行业项目组', null, '6', null, '6', '', '0', '2020-03-15 13:16:57', '2020-03-16 00:43:03'), ('7', '1', '测试部', '测试部', null, '5', null, '6', '', '0', '2020-03-15 13:17:19', '2020-03-16 00:43:14'), ('8', '1', '设计部', 'UI设计部门', null, '5', null, '7', '', '0', '2020-03-15 13:17:56', '2020-03-16 00:43:18'), ('9', '1', '市场部', '市场部', null, '5', null, '8', '', '0', '2020-03-15 13:18:15', '2020-03-16 00:43:23');
COMMIT;

-- ----------------------------
--  Table structure for `sys_pays`
-- ----------------------------
DROP TABLE IF EXISTS `sys_pays`;
CREATE TABLE `sys_pays` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `driver` varchar(255) DEFAULT NULL COMMENT '驱动',
  `config` longtext COMMENT '配置',
  `comment` varchar(255) DEFAULT NULL COMMENT '说明',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `is_mobile` int DEFAULT NULL COMMENT '移动端',
  `is_pc` int DEFAULT NULL COMMENT 'pc端',
  `is_handling_fee` int DEFAULT NULL COMMENT '手续费tag',
  `handling_fee` int DEFAULT NULL COMMENT '手续费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COMMENT='支付配置';

-- ----------------------------
--  Records of `sys_pays`
-- ----------------------------
BEGIN;
INSERT INTO `sys_pays` VALUES ('1', '支付宝', 'mqpay_alipay', '{\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', 'V免签 - 支付宝（费率0）（自己搭建）', '2021-03-29 16:16:05', '2021-03-29 16:16:07', '0', '0', null, '0'), ('2', '微信', 'mqpay_wxpay', '{\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', 'V免签 - 微信 （费率0）（自己搭建）', '2021-03-29 16:17:52', '2021-03-29 16:17:55', '0', '0', null, '0'), ('3', 'QQ钱包', 'epay_qqpay', '{\"pid\":\"xxx\",\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', '易支付 - QQ钱包', '2021-05-24 16:12:49', '2021-10-30 16:12:49', '0', '0', '0', '0'), ('5', '支付宝', 'epay_alipay', '{\"pid\":\"xxx\",\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', '易支付 - 支付宝', '2021-05-24 12:00:01', '2021-05-24 12:00:04', '0', '0', null, '0'), ('6', '微信', 'epay_wxpay', '{\"pid\":\"xxx\",\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', '易支付 - 微信', '2021-05-24 12:00:55', '2021-05-24 12:01:00', '0', '0', null, '0'), ('7', '微信', 'yungouos_wxpay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxxx\"}', 'YunGouOS - 微信（个人小薇支付-费率低） 申请地址：https://dwz.cn/QQLN87nX', '2021-06-06 04:53:12', '2021-06-06 04:53:20', '0', '0', null, '0'), ('8', '支付宝', 'yungouos_alipay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', 'YunGouOS - 支付宝 （个人小薇支付-费率低） 申请地址：https://dwz.cn/QQLN87nX', '2021-06-06 04:54:03', '2021-06-06 04:54:06', '0', '0', null, '0'), ('9', '微信', 'xunhupay_wxpay', '{\"appid\":\"xxx\",\"appsecret\":\"xxx\",\"notify_url\":\"xxx\",\"create_url\":\"https://api.xunhupay.com/payment/do.html\"}', '虎皮椒V3 - 微信（费率-H5版2%/普通版1%） 申请地址：https://www.xunhupay.com', '2021-06-06 22:24:47', '2021-06-06 22:24:50', '0', '0', null, '0'), ('10', '支付宝', 'xunhupay_alipay', '{\"appid\":\"xxx\",\"appsecret\":\"xxx\",\"notify_url\":\"xxx\"}', '虎皮椒V3 - 支付宝 申请地址：https://www.xunhupay.com', '2021-06-06 22:25:19', '2021-06-06 22:25:22', '0', '0', null, '0'), ('13', '微信', 'payjs_wxpay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', 'Payjs - 微信扫码 申请地址：https://payjs.cn', '2021-06-27 14:17:54', '2021-06-27 14:17:58', '0', '0', null, '0'), ('14', '支付宝', 'payjs_alipay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', 'Payjs - 支付宝扫码 申请地址：https://payjs.cn', '2021-06-27 14:18:38', '2021-06-27 14:18:43', '0', '0', null, '0'), ('17', '微信', 'wxpay', '{\"mchId\":\"xxx\",\"appId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', '官方微信 - 扫码支付', '2021-07-02 02:47:37', '2021-07-02 02:47:40', '0', '0', null, '0'), ('18', '支付宝', 'alipay', '{\"private_key\":\"xxx\",\"notify_url\":\"xxx\",\"app_id\":\"xxx\",\"alipay_public_key\":\"xxx\"}', '官方支付宝 - 当面付', '2021-07-03 18:53:08', '2021-07-02 18:53:11', '0', '0', '0', '0'), ('19', '微信H5', 'wxpay_h5', '{\"mchId\":\"xxx\",\"appId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', '官方微信 - H5支付 （开启后只在手机端显示）', '2021-07-02 23:20:54', '2021-08-17 23:20:56', '0', '0', null, '0'), ('20', 'Paypal', 'paypal', '{\"clientId\":\"xxx\",\"return_url\":\"xxx\",\"clientSecret\":\"xxx\"}', 'Paypal 境外支付（默认美元交易）', '2021-08-24 12:04:25', '2021-08-24 12:04:28', '0', '0', '0', '0'), ('22', '支付宝PC', 'alipay_pc', '{\"private_key\":\"xxx\",\"notify_url\":\"xxx\",\"app_id\":\"xxx\",\"alipay_public_key\":\"xxx\"}', '支付宝官方 - pc端支付', '2021-07-03 03:04:02', '2021-11-03 03:04:02', '1', '1', '0', '0'), ('23', 'USDT', 'epusdt', '{\"notify_url\":\"xxx\",\"create_url\":\"填写收银台域名后面不变/api/v1/order/create-transaction\",\"key\":\"xxx\"}', 'Epusdt TRC-20 数字货币 自己搭建【地址：https://github.com/assimon/epusdt】', '2022-09-15 15:33:41', '2022-09-15 15:33:43', '1', '1', '1', '8');
COMMIT;

-- ----------------------------
--  Table structure for `sys_products`
-- ----------------------------
DROP TABLE IF EXISTS `sys_products`;
CREATE TABLE `sys_products` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `price` decimal(18,2) DEFAULT NULL COMMENT '商品金额',
  `sort` int DEFAULT NULL COMMENT '排序',
  `link` varchar(255) DEFAULT NULL COMMENT '商品链接',
  `status` int DEFAULT NULL COMMENT '商品状态',
  `pd_info` longtext COMMENT '商品详情',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted_at` datetime DEFAULT NULL COMMENT '删除时间',
  `classify_id` int DEFAULT NULL COMMENT '分类id',
  `index_logo` longtext COMMENT '首页截图',
  `image_logo` longtext COMMENT '商品logo',
  `is_wholesale` int DEFAULT NULL COMMENT '批发功能',
  `wholesale` longtext COMMENT '批发配置',
  `restricts` int DEFAULT '0' COMMENT '限制购买',
  `ship_type` int DEFAULT NULL COMMENT '发货类型（0-自动，1-手动）',
  `inventory` int DEFAULT NULL COMMENT '商品库存（人工发货类型生效）',
  `sales` int DEFAULT NULL COMMENT '销量',
  `is_password` int DEFAULT NULL COMMENT '是否开启密码查询',
  `customize_input` longtext COMMENT '自定义输入框',
  `is_customize` int DEFAULT NULL COMMENT '是否开启自定义输入框',
  `sell_type` int DEFAULT NULL COMMENT '售卡类型',
  `component_point` varchar(255) NOT NULL COMMENT '提示语',
  `component_type` int DEFAULT NULL COMMENT '提示类型',
  `component_url` varchar(255) DEFAULT NULL COMMENT '提示链接',
  `component_enabled` int DEFAULT NULL COMMENT '是否开启提示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='商品';

-- ----------------------------
--  Records of `sys_products`
-- ----------------------------
BEGIN;
INSERT INTO `sys_products` VALUES ('9', '官方分类', '20.00', '1000', 'U545gGSf1j0Q1HUJ', '1', '', '2022-10-05 23:06:40', '2022-10-05 23:06:40', '2022-10-05 23:06:40', '1', '', '', '0', '', '0', '0', null, null, '1', null, '0', '0', '', '1', '', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(200) NOT NULL COMMENT '角色名称',
  `role_code` varchar(200) DEFAULT NULL COMMENT '角色标识',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色';

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', '管理员', 'admin', '管理员', '0', '2020-02-26 15:18:37', '2020-03-21 15:15:54'), ('2', '普通用户', 'user', '普通用户', '0', '2020-02-26 15:18:52', '2020-03-21 15:16:02'), ('3', '游客', 'guest', '游客', '0', '2020-02-26 15:19:49', '2020-03-21 15:16:57');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` int NOT NULL COMMENT '菜单id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_role_permission_role` (`role_id`),
  KEY `menu_id` (`menu_id`),
  CONSTRAINT `sys_role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`menu_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=763 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色权限';

-- ----------------------------
--  Records of `sys_role_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES ('656', '1', '75', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('657', '1', '37', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('658', '1', '38', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('659', '1', '39', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('660', '1', '40', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('661', '1', '41', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('662', '1', '42', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('663', '1', '76', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('664', '1', '43', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('665', '1', '44', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('666', '1', '56', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('667', '1', '45', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('668', '1', '46', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('669', '1', '47', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('670', '1', '48', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('671', '1', '54', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('672', '1', '51', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('673', '1', '49', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('674', '1', '50', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('675', '1', '52', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('676', '1', '53', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('677', '1', '63', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('678', '1', '64', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('679', '1', '65', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('680', '1', '66', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('681', '1', '67', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('682', '1', '68', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('683', '1', '83', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('684', '1', '89', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('685', '1', '84', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('686', '1', '85', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('687', '1', '86', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('688', '1', '87', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('689', '1', '88', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('690', '1', '103', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('691', '1', '109', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('692', '1', '110', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('693', '1', '104', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('694', '1', '105', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('695', '1', '106', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('696', '1', '107', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('697', '1', '108', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('698', '1', '117', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('699', '1', '118', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('700', '1', '112', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('701', '1', '113', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('702', '1', '114', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('703', '1', '115', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('704', '1', '116', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('705', '1', '1', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('706', '1', '2', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('707', '1', '3', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('708', '1', '4', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('709', '1', '5', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('710', '1', '6', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('711', '1', '7', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('712', '1', '8', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('713', '1', '9', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('714', '1', '10', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('715', '1', '11', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('716', '1', '12', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('717', '1', '13', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('718', '1', '14', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('719', '1', '15', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('720', '1', '16', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('721', '1', '17', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('722', '1', '18', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('723', '1', '19', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('724', '1', '20', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('725', '1', '21', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('726', '1', '22', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('727', '1', '23', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('728', '1', '24', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('729', '1', '25', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('730', '1', '26', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('731', '1', '57', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('732', '1', '97', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('733', '1', '98', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('734', '1', '99', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('735', '1', '100', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('736', '1', '101', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('737', '1', '58', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('738', '1', '59', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('739', '1', '60', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('740', '1', '61', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('741', '1', '62', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('742', '1', '102', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('743', '1', '91', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('744', '1', '92', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('745', '1', '93', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('746', '1', '94', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('747', '1', '95', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('748', '1', '78', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('749', '1', '79', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('750', '1', '80', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('751', '1', '81', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('752', '1', '82', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('753', '1', '27', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('754', '1', '28', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('755', '1', '29', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('756', '1', '30', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('757', '1', '31', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('758', '1', '32', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('759', '1', '33', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('760', '1', '34', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('761', '1', '35', '2021-11-10 03:07:03', '2021-11-10 03:07:03'), ('762', '1', '36', '2021-11-10 03:07:03', '2021-11-10 03:07:03');
COMMIT;

-- ----------------------------
--  Table structure for `sys_shop_settings`
-- ----------------------------
DROP TABLE IF EXISTS `sys_shop_settings`;
CREATE TABLE `sys_shop_settings` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `is_window` int DEFAULT NULL COMMENT '是否开启弹窗',
  `window_text` longtext COMMENT '弹窗内容',
  `is_background` varchar(255) DEFAULT NULL COMMENT '全局背景图',
  `store_details` longtext COMMENT '商店详情',
  `is_wxpusher` int DEFAULT NULL COMMENT '是否开启微信通知',
  `app_token` varchar(255) DEFAULT NULL COMMENT 'pusher token',
  `wxpush_uid` varchar(255) DEFAULT NULL COMMENT '微信通知uid',
  `is_email` int DEFAULT '0' COMMENT '邮件通知开关',
  `is_list_layout` int DEFAULT NULL COMMENT '是否开启列表布局',
  `quotations` varchar(255) DEFAULT NULL COMMENT '首页语录',
  `qq_customer_service` varchar(255) DEFAULT NULL COMMENT 'QQ客服',
  `qq_group_qrcode` varchar(255) DEFAULT NULL COMMENT 'QQ群二维码',
  `tg_customer_service` varchar(255) DEFAULT NULL COMMENT 'TG客服',
  `is_client` int DEFAULT NULL COMMENT '是否开启客服',
  `crisp_key` varchar(255) DEFAULT NULL COMMENT 'crisp密钥',
  `is_model` int DEFAULT NULL COMMENT '首页模版',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='商店设置';

-- ----------------------------
--  Records of `sys_shop_settings`
-- ----------------------------
BEGIN;
INSERT INTO `sys_shop_settings` VALUES ('1', '1', '<div class=\"index-module_textWrap_3ygOc\">\n<p><span class=\"bjh-p\">&nbsp; &nbsp; 想把世界最好的给你，却发现世上最好的是你；我不要不老的青春，只要一个盗不走的爱人。</span></p>\n<p>&nbsp;</p>\n</div>\n<div class=\"index-module_textWrap_3ygOc\">\n<p><span class=\"bjh-p\">&nbsp; &nbsp; 有时幸福就像手心里的沙，握得越紧，失去得越快；有时幸福就像隔岸的花朵，隐约可见，却无法触摸。两个人的世界里，总要一个闹着、一个笑着、一个吵着、一个哄着。</span></p>\n</div>', 'https://s1.hdslb.com/bfs/static/blive/blfe-dynamic-web/static/img/background.bc725153.png', '<p>打开祝福的心扉，让梦想在晨光中翱翔。</p>', '1', 'xxx', 'xxx', '0', '1', '打开祝福的心扉，让梦想在晨光中翱翔。', '1724962375', 'xxx', 'xxx', '1', '3ff64218-c586-4c42-91fe-c13c8aa07405', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_theme`
-- ----------------------------
DROP TABLE IF EXISTS `sys_theme`;
CREATE TABLE `sys_theme` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) DEFAULT NULL COMMENT '主题名称',
  `description` longtext COMMENT '说明',
  `driver` varchar(255) DEFAULT NULL COMMENT '主题驱动',
  `enable` int DEFAULT NULL COMMENT '是否设置',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='主题配置';

-- ----------------------------
--  Records of `sys_theme`
-- ----------------------------
BEGIN;
INSERT INTO `sys_theme` VALUES ('1', '系统默认', '系统预设主题', 'default', '1', '2021-06-28 00:58:32', '2021-06-28 00:58:34');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '账号',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `nick_name` varchar(200) NOT NULL COMMENT '昵称',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `sex` int DEFAULT NULL COMMENT '性别',
  `phone` varchar(200) DEFAULT NULL COMMENT '手机号',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `email_verified` int NOT NULL DEFAULT '0' COMMENT '邮箱是否验证,0否,1是',
  `true_name` varchar(200) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(200) DEFAULT NULL COMMENT '身份证号',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `introduction` varchar(200) DEFAULT NULL COMMENT '个人简介',
  `organization_id` int DEFAULT NULL COMMENT '机构id',
  `state` int NOT NULL DEFAULT '0' COMMENT '状态,0正常,1冻结',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `qq_uuid` varchar(255) DEFAULT NULL COMMENT 'qquuid',
  `wx_uuid` varchar(255) DEFAULT NULL COMMENT 'wxuuid',
  PRIMARY KEY (`user_id`),
  KEY `organization_id` (`organization_id`),
  CONSTRAINT `sys_user_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `sys_organization` (`organization_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', null, '1', '', null, '0', null, null, null, '312312344444', null, '0', '0', '2020-01-13 14:43:52', '2022-04-12 15:25:09', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int NOT NULL COMMENT '用户id',
  `role_id` int NOT NULL COMMENT '角色id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_user_role` (`user_id`),
  KEY `FK_sys_user_role_role` (`role_id`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户角色';

-- ----------------------------
--  Records of `sys_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('2', '1', '1', '2021-08-23 18:29:06', '2021-08-23 18:29:06');
COMMIT;

-- ----------------------------
--  Table structure for `sys_website`
-- ----------------------------
DROP TABLE IF EXISTS `sys_website`;
CREATE TABLE `sys_website` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `website_name` varchar(255) DEFAULT NULL COMMENT '网站名称',
  `website_url` varchar(255) DEFAULT NULL COMMENT '网站域名',
  `website_logo` varchar(255) DEFAULT '' COMMENT '网站logo',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `beian_icp` varchar(255) DEFAULT NULL COMMENT '备案ICP',
  `keywords` longtext COMMENT '关键字',
  `description` longtext COMMENT '网站描述',
  `favicon` varchar(255) DEFAULT NULL COMMENT 'favicon',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='网站设置';

-- ----------------------------
--  Records of `sys_website`
-- ----------------------------
BEGIN;
INSERT INTO `sys_website` VALUES ('1', '波猫商店', 'http://free.bomaos.com', '', '1724962375', 'Copyright © 2012-2022 波猫商店', '商城系统,商城源码,tg营销助手,发卡网', '波猫商店 - 全新UI商城系统', '');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
