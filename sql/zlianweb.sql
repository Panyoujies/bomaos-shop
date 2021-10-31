/*
Navicat MySQL Data Transfer

Source Server         : 本地环境
Source Server Version : 50733
Source Host           : localhost:3306
Source Database       : zlianweb

Target Server Type    : MYSQL
Target Server Version : 50733
File Encoding         : 65001

Date: 2021-08-24 16:50:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for oauth_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_token`;
CREATE TABLE `oauth_token` (
  `token_id` int(11) NOT NULL AUTO_INCREMENT,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for oauth_token_key
-- ----------------------------
DROP TABLE IF EXISTS `oauth_token_key`;
CREATE TABLE `oauth_token_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token_key` varchar(128) NOT NULL COMMENT '生成token时的key',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_cards
-- ----------------------------
DROP TABLE IF EXISTS `sys_cards`;
CREATE TABLE `sys_cards` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_id` int(200) DEFAULT NULL COMMENT '对应商品id',
  `card_info` text COMMENT '卡密',
  `status` int(1) DEFAULT NULL COMMENT '卡密状态',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卡密';

-- ----------------------------
-- Table structure for sys_classifys
-- ----------------------------
DROP TABLE IF EXISTS `sys_classifys`;
CREATE TABLE `sys_classifys` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(200) DEFAULT NULL COMMENT '分类名称',
  `status` int(1) DEFAULT NULL COMMENT '分类状态',
  `sort` int(200) DEFAULT NULL COMMENT '排序',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类';

-- ----------------------------
-- Table structure for sys_coupon
-- ----------------------------
DROP TABLE IF EXISTS `sys_coupon`;
CREATE TABLE `sys_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `classifys_id` int(11) DEFAULT NULL COMMENT '分类id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `type` int(1) DEFAULT NULL COMMENT '类型-0一次性，1重复使用',
  `status` int(1) DEFAULT NULL COMMENT '状态',
  `coupon` varchar(255) DEFAULT NULL COMMENT '优惠券代码',
  `discount_type` int(1) DEFAULT NULL COMMENT '面额或者百分比',
  `discount_val` decimal(18,2) DEFAULT NULL COMMENT '面额、折扣 价格和百分比',
  `count_used` int(11) DEFAULT NULL COMMENT '已使用次数',
  `count_all` int(11) DEFAULT NULL COMMENT '可用次数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `full_reduction` decimal(18,2) DEFAULT NULL COMMENT '满减金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券';

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary` (
  `dict_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `dict_code` varchar(100) NOT NULL COMMENT '字典标识',
  `dict_name` varchar(200) NOT NULL COMMENT '字典名称',
  `sort_number` int(11) NOT NULL DEFAULT '1' COMMENT '排序号',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='字典';

-- ----------------------------
-- Records of sys_dictionary
-- ----------------------------
INSERT INTO `sys_dictionary` VALUES ('1', 'sex', '性别', '1', '', '0', '2020-03-15 13:04:39', '2020-03-15 13:04:39');
INSERT INTO `sys_dictionary` VALUES ('2', 'organization_type', '机构类型', '2', '', '0', '2020-03-16 00:32:36', '2020-03-16 00:32:36');

-- ----------------------------
-- Table structure for sys_dictionary_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary_data`;
CREATE TABLE `sys_dictionary_data` (
  `dict_data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典项id',
  `dict_id` int(11) NOT NULL COMMENT '字典id',
  `dict_data_code` varchar(100) NOT NULL COMMENT '字典项标识',
  `dict_data_name` varchar(200) NOT NULL COMMENT '字典项名称',
  `sort_number` int(11) NOT NULL DEFAULT '1' COMMENT '排序号',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_data_id`),
  KEY `dict_id` (`dict_id`),
  CONSTRAINT `sys_dictionary_data_ibfk_1` FOREIGN KEY (`dict_id`) REFERENCES `sys_dictionary` (`dict_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='字典项';

-- ----------------------------
-- Records of sys_dictionary_data
-- ----------------------------
INSERT INTO `sys_dictionary_data` VALUES ('1', '1', 'male', '男', '1', '', '0', '2020-03-15 13:07:28', '2020-03-15 13:07:28');
INSERT INTO `sys_dictionary_data` VALUES ('2', '1', 'female', '女', '2', '', '0', '2020-03-15 13:07:41', '2020-03-15 15:58:04');
INSERT INTO `sys_dictionary_data` VALUES ('3', '2', 'company', '公司', '1', '', '0', '2020-03-16 00:34:32', '2020-03-16 00:34:32');
INSERT INTO `sys_dictionary_data` VALUES ('4', '2', 'subsidiary', '子公司', '2', '', '0', '2020-03-16 00:35:02', '2020-03-16 00:35:02');
INSERT INTO `sys_dictionary_data` VALUES ('5', '2', 'department', '部门', '3', '', '0', '2020-03-16 00:35:18', '2020-03-16 00:35:18');
INSERT INTO `sys_dictionary_data` VALUES ('6', '2', 'group', '小组', '4', '', '0', '2020-03-16 00:35:36', '2020-03-16 00:35:36');

-- ----------------------------
-- Table structure for sys_login_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_record`;
CREATE TABLE `sys_login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) NOT NULL COMMENT '用户账号',
  `os` varchar(200) DEFAULT NULL COMMENT '操作系统',
  `device` varchar(200) DEFAULT NULL COMMENT '设备名',
  `browser` varchar(200) DEFAULT NULL COMMENT '浏览器类型',
  `ip` varchar(200) DEFAULT NULL COMMENT 'ip地址',
  `oper_type` int(11) NOT NULL COMMENT '操作类型,0登录成功,1登录失败,2退出登录,3刷新token',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='登录日志';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级id,0是顶级',
  `menu_name` varchar(200) NOT NULL COMMENT '菜单名称',
  `menu_icon` varchar(200) DEFAULT NULL COMMENT '菜单图标',
  `path` varchar(200) DEFAULT NULL COMMENT '菜单地址',
  `menu_type` int(11) DEFAULT '0' COMMENT '类型,0菜单,1按钮',
  `sort_number` int(11) NOT NULL DEFAULT '1' COMMENT '排序号',
  `authority` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `target` varchar(200) DEFAULT '_self' COMMENT '打开位置',
  `icon_color` varchar(200) DEFAULT NULL COMMENT '图标颜色',
  `hide` int(11) NOT NULL DEFAULT '0' COMMENT '是否隐藏,0否,1是',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COMMENT='菜单';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', 'layui-icon layui-icon-set-sm', '', '0', '997', '', '_self', null, '0', '0', '2020-02-26 12:51:23', '2021-03-27 20:25:24');
INSERT INTO `sys_menu` VALUES ('2', '1', '用户管理', null, 'sys/user', '0', '1', 'sys:user:view', '_self', null, '0', '0', '2020-02-26 12:51:55', '2020-03-21 18:45:26');
INSERT INTO `sys_menu` VALUES ('3', '2', '查询用户', null, null, '1', '1', 'sys:user:list', '_self', null, '0', '0', '2020-02-26 12:52:06', '2020-03-21 18:45:28');
INSERT INTO `sys_menu` VALUES ('4', '2', '添加用户', null, null, '1', '2', 'sys:user:save', '_self', null, '0', '0', '2020-02-26 12:52:26', '2020-03-21 18:45:29');
INSERT INTO `sys_menu` VALUES ('5', '2', '修改用户', null, null, '1', '3', 'sys:user:update', '_self', null, '0', '0', '2020-02-26 12:52:50', '2020-03-21 18:45:30');
INSERT INTO `sys_menu` VALUES ('6', '2', '删除用户', null, null, '1', '4', 'sys:user:remove', '_self', null, '0', '0', '2020-02-26 12:53:13', '2020-03-21 18:45:32');
INSERT INTO `sys_menu` VALUES ('7', '1', '角色管理', null, 'sys/role', '0', '2', 'sys:role:view', '_self', null, '0', '0', '2020-03-13 13:29:08', '2020-03-21 18:45:33');
INSERT INTO `sys_menu` VALUES ('8', '7', '查询角色', null, null, '1', '1', 'sys:role:list', '_self', null, '0', '0', '2020-03-13 13:30:41', '2020-03-21 18:45:34');
INSERT INTO `sys_menu` VALUES ('9', '7', '添加角色', null, null, '1', '2', 'sys:role:save', '_self', null, '0', '0', '2020-03-15 13:02:07', '2020-03-21 18:45:35');
INSERT INTO `sys_menu` VALUES ('10', '7', '修改角色', null, null, '1', '3', 'sys:role:update', '_self', null, '0', '0', '2020-03-15 13:02:49', '2020-03-21 18:45:36');
INSERT INTO `sys_menu` VALUES ('11', '7', '删除角色', null, null, '1', '4', 'sys:role:remove', '_self', null, '0', '0', '2020-03-20 17:58:51', '2020-03-21 18:45:38');
INSERT INTO `sys_menu` VALUES ('12', '1', '菜单管理', null, 'sys/menu', '0', '3', 'sys:menu:view', '_self', null, '0', '0', '2020-03-21 01:07:13', '2020-03-21 18:45:39');
INSERT INTO `sys_menu` VALUES ('13', '12', '查询菜单', null, null, '1', '1', 'sys:menu:list', '_self', null, '0', '0', '2020-03-21 16:43:30', '2020-03-21 18:45:40');
INSERT INTO `sys_menu` VALUES ('14', '12', '添加菜单', null, null, '1', '2', 'sys:menu:save', '_self', null, '0', '0', '2020-03-21 16:43:54', '2020-03-21 18:45:41');
INSERT INTO `sys_menu` VALUES ('15', '12', '修改菜单', null, null, '1', '3', 'sys:menu:update', '_self', null, '0', '0', '2020-03-21 18:24:17', '2020-03-21 18:45:43');
INSERT INTO `sys_menu` VALUES ('16', '12', '删除菜单', null, null, '1', '4', 'sys:menu:remove', '_self', null, '0', '0', '2020-03-21 18:24:18', '2020-03-21 18:45:44');
INSERT INTO `sys_menu` VALUES ('17', '1', '机构管理', null, 'sys/organization', '0', '4', 'sys:org:view', '_self', null, '0', '0', '2020-03-21 18:24:20', '2020-03-21 18:45:49');
INSERT INTO `sys_menu` VALUES ('18', '17', '查询机构', null, null, '1', '1', 'sys:org:list', '_self', null, '0', '0', '2020-03-21 18:24:21', '2020-03-21 18:44:36');
INSERT INTO `sys_menu` VALUES ('19', '17', '添加机构', null, null, '1', '2', 'sys:org:save', '_self', null, '0', '0', '2020-03-21 18:24:22', '2020-03-21 18:45:51');
INSERT INTO `sys_menu` VALUES ('20', '17', '修改机构', null, null, '1', '3', 'sys:org:update', '_self', null, '0', '0', '2020-03-21 18:24:24', '2020-03-21 18:45:52');
INSERT INTO `sys_menu` VALUES ('21', '17', '删除机构', null, null, '1', '4', 'sys:org:remove', '_self', null, '0', '0', '2020-03-21 18:24:25', '2020-03-21 18:45:54');
INSERT INTO `sys_menu` VALUES ('22', '1', '字典管理', null, 'sys/dict', '0', '5', 'sys:dict:view', '_self', null, '0', '0', '2020-03-21 18:24:26', '2020-03-21 18:45:56');
INSERT INTO `sys_menu` VALUES ('23', '22', '查询字典', null, null, '1', '1', 'sys:dict:list', '_self', null, '0', '0', '2020-03-21 18:24:27', '2020-03-21 18:44:42');
INSERT INTO `sys_menu` VALUES ('24', '22', '添加字典', null, null, '1', '2', 'sys:dict:save', '_self', null, '0', '0', '2020-03-21 18:24:28', '2020-03-21 18:45:59');
INSERT INTO `sys_menu` VALUES ('25', '22', '修改字典', null, null, '1', '3', 'sys:dict:update', '_self', null, '0', '0', '2020-03-21 18:24:29', '2020-03-21 18:46:01');
INSERT INTO `sys_menu` VALUES ('26', '22', '删除字典', null, null, '1', '4', 'sys:dict:remove', '_self', null, '0', '0', '2020-03-21 18:24:31', '2020-03-21 18:46:02');
INSERT INTO `sys_menu` VALUES ('27', '0', '日志管理', 'layui-icon layui-icon-list', '', '0', '998', '', '_self', null, '0', '0', '2020-03-21 18:24:32', '2021-03-27 20:25:31');
INSERT INTO `sys_menu` VALUES ('28', '27', '登录日志', null, 'sys/loginRecord', '0', '1', 'sys:login_record:view', '_self', null, '0', '0', '2020-03-21 18:24:33', '2020-03-21 18:44:52');
INSERT INTO `sys_menu` VALUES ('29', '27', '操作日志', null, 'sys/operRecord', '0', '2', 'sys:oper_record:view', '_self', null, '0', '0', '2020-03-21 18:24:34', '2020-03-21 18:46:10');
INSERT INTO `sys_menu` VALUES ('30', '27', '数据监控', null, 'druid', '0', '3', null, '_self', null, '0', '0', '2020-03-21 18:24:35', '2020-03-22 14:46:21');
INSERT INTO `sys_menu` VALUES ('31', '0', '系统工具', 'layui-icon layui-icon-slider', '', '0', '999', '', '_self', null, '0', '0', '2020-03-21 18:24:36', '2021-03-27 20:25:39');
INSERT INTO `sys_menu` VALUES ('32', '31', '文件管理', null, 'file/manage', '0', '1', 'sys:file:view', '_self', null, '0', '0', '2020-03-21 18:24:38', '2020-03-22 14:46:57');
INSERT INTO `sys_menu` VALUES ('33', '32', '查询文件', null, null, '1', '1', 'sys:file:list', '_self', null, '0', '0', '2020-03-21 18:24:39', '2020-03-22 14:47:32');
INSERT INTO `sys_menu` VALUES ('34', '32', '删除文件', null, null, '1', '2', 'sys:file:remove', '_self', null, '0', '0', '2020-03-21 18:24:40', '2020-03-22 14:46:54');
INSERT INTO `sys_menu` VALUES ('35', '31', '发送邮件', null, 'sys/email', '0', '2', 'sys:email:view', '_self', null, '0', '0', '2020-03-21 18:24:41', '2020-03-22 14:47:35');
INSERT INTO `sys_menu` VALUES ('36', '31', '项目生成', null, null, '0', '3', null, '_self', null, '0', '0', '2020-03-21 18:24:42', '2020-03-22 14:47:36');
INSERT INTO `sys_menu` VALUES ('37', '0', '商品管理', 'layui-icon layui-icon-cart-simple', '', '0', '2', '', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-30 02:17:40');
INSERT INTO `sys_menu` VALUES ('38', '37', '分类管理', '', 'products/classifys', '0', '1', 'products:classifys:view', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:26:54');
INSERT INTO `sys_menu` VALUES ('39', '38', '查询分类', '', '', '1', '1', 'products:classifys:list', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('40', '38', '添加分类', '', '', '1', '2', 'products:classifys:save', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('41', '38', '修改分类', '', '', '1', '3', 'products:classifys:update', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('42', '38', '删除分类', '', '', '1', '4', 'products:classifys:remove', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('43', '37', '商品管理', '', 'products/products', '0', '3', 'products:products:view', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-06-05 15:51:44');
INSERT INTO `sys_menu` VALUES ('44', '43', '查询商品', '', '', '1', '1', 'products:products:list', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('45', '43', '添加商品', '', '', '1', '2', 'products:products:save', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('46', '43', '修改商品', '', '', '1', '3', 'products:products:update', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('47', '43', '删除商品', '', '', '1', '4', 'products:products:remove', '_self', null, '0', '0', '2021-03-27 20:23:20', '2021-03-27 20:23:20');
INSERT INTO `sys_menu` VALUES ('48', '0', '卡密管理', 'layui-icon layui-icon-template-1', '', '0', '3', '', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-30 02:18:38');
INSERT INTO `sys_menu` VALUES ('49', '48', '卡密管理', '', 'carmi/cards', '0', '2', 'carmi:cards:view', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:46:41');
INSERT INTO `sys_menu` VALUES ('50', '49', '查询卡密', '', '', '1', '1', 'carmi:cards:list', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:34:17');
INSERT INTO `sys_menu` VALUES ('51', '54', '添加卡密', '', '', '1', '2', 'carmi:cards:save', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:46:54');
INSERT INTO `sys_menu` VALUES ('52', '49', '修改卡密', '', '', '1', '3', 'carmi:cards:update', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:34:17');
INSERT INTO `sys_menu` VALUES ('53', '49', '删除卡密', '', '', '1', '4', 'carmi:cards:remove', '_self', null, '0', '0', '2021-03-28 00:34:17', '2021-03-28 00:34:17');
INSERT INTO `sys_menu` VALUES ('54', '48', '添加卡密', '', 'carmi/cards/add', '0', '1', 'carmi:cards:view', '_self', null, '0', '0', '2021-03-28 00:46:11', '2021-03-28 00:46:26');
INSERT INTO `sys_menu` VALUES ('55', '48', '售出卡密', '', 'carmi/cards/sold', '0', '3', 'carmi:cards:view', '_self', null, '0', '0', '2021-03-28 14:36:53', '2021-03-28 14:36:53');
INSERT INTO `sys_menu` VALUES ('56', '55', '查询卡密', '', '', '1', '1', 'carmi:cards:list', '_self', null, '0', '0', '2021-03-28 14:37:26', '2021-03-28 14:37:26');
INSERT INTO `sys_menu` VALUES ('57', '0', '系统配置', 'layui-icon layui-icon-set-fill', '', '0', '8', '', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-06-23 07:54:38');
INSERT INTO `sys_menu` VALUES ('58', '57', '支付配置', '', 'settings/pays', '0', '2', 'settings:pays:view', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-07-04 04:01:04');
INSERT INTO `sys_menu` VALUES ('59', '58', '查询支付配置', '', '', '1', '1', 'settings:pays:list', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27');
INSERT INTO `sys_menu` VALUES ('60', '58', '添加支付配置', '', '', '1', '2', 'settings:pays:save', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27');
INSERT INTO `sys_menu` VALUES ('61', '58', '修改支付配置', '', '', '1', '3', 'settings:pays:update', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27');
INSERT INTO `sys_menu` VALUES ('62', '58', '删除支付配置', '', '', '1', '4', 'settings:pays:remove', '_self', null, '0', '0', '2021-03-29 11:07:27', '2021-03-29 11:07:27');
INSERT INTO `sys_menu` VALUES ('63', '0', '订单管理', 'layui-icon layui-icon-rmb', '', '0', '4', '', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-30 02:18:55');
INSERT INTO `sys_menu` VALUES ('64', '63', '订单流水', '', 'orders/orders', '0', '1', 'orders:orders:view', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 17:01:42');
INSERT INTO `sys_menu` VALUES ('65', '64', '查询订单表', '', '', '1', '1', 'orders:orders:list', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07');
INSERT INTO `sys_menu` VALUES ('66', '64', '添加订单表', '', '', '1', '2', 'orders:orders:save', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07');
INSERT INTO `sys_menu` VALUES ('67', '64', '修改订单表', '', '', '1', '3', 'orders:orders:update', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07');
INSERT INTO `sys_menu` VALUES ('68', '64', '删除订单表', '', '', '1', '4', 'orders:orders:remove', '_self', null, '0', '0', '2021-03-29 16:26:07', '2021-03-29 16:26:07');
INSERT INTO `sys_menu` VALUES ('69', '0', '卡密订单关联', 'layui-icon layui-icon-senior', '', '0', '999', 'carmi:orderCard:view', '_self', null, '1', '0', '2021-03-29 22:28:45', '2021-03-30 00:58:53');
INSERT INTO `sys_menu` VALUES ('70', '69', '订单关联卡密表管理', '', 'carmi/orderCard', '0', '1', 'carmi:orderCard:view', '_self', null, '0', '0', '2021-03-29 22:28:45', '2021-03-29 22:28:45');
INSERT INTO `sys_menu` VALUES ('71', '70', '查询订单关联卡密表', '', '', '1', '1', 'carmi:orderCard:list', '_self', null, '0', '0', '2021-03-29 22:28:45', '2021-03-29 22:28:45');
INSERT INTO `sys_menu` VALUES ('72', '70', '添加订单关联卡密表', '', '', '1', '2', 'carmi:orderCard:save', '_self', null, '0', '0', '2021-03-29 22:28:45', '2021-03-29 22:28:45');
INSERT INTO `sys_menu` VALUES ('73', '70', '修改订单关联卡密表', '', '', '1', '3', 'carmi:orderCard:update', '_self', null, '0', '0', '2021-03-29 22:28:45', '2021-03-29 22:28:45');
INSERT INTO `sys_menu` VALUES ('74', '70', '删除订单关联卡密表', '', '', '1', '4', 'carmi:orderCard:remove', '_self', null, '0', '0', '2021-03-29 22:28:45', '2021-03-29 22:28:45');
INSERT INTO `sys_menu` VALUES ('75', '0', '管理中心', 'layui-icon layui-icon-console', 'dashboard/workplace', '0', '1', 'dashboard:user:view', '_self', null, '0', '0', '2021-03-30 00:58:26', '2021-03-30 02:16:33');
INSERT INTO `sys_menu` VALUES ('76', '37', '添加商品', '', 'products/products/addProduct', '0', '2', 'products:products:view', '_self', null, '0', '0', '2021-06-05 15:51:32', '2021-06-05 15:52:52');
INSERT INTO `sys_menu` VALUES ('77', '0', 'website', 'layui-icon layui-icon-senior', '', '0', '1', '', '_self', null, '0', '1', '2021-06-06 02:17:34', '2021-06-06 02:19:37');
INSERT INTO `sys_menu` VALUES ('78', '57', '网站设置', '', 'website/website', '0', '4', 'website:website:view', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-07-04 04:01:28');
INSERT INTO `sys_menu` VALUES ('79', '78', '查询网站设置', '', '', '1', '1', 'website:website:list', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34');
INSERT INTO `sys_menu` VALUES ('80', '78', '添加网站设置', '', '', '1', '2', 'website:website:save', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34');
INSERT INTO `sys_menu` VALUES ('81', '78', '修改网站设置', '', '', '1', '3', 'website:website:update', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34');
INSERT INTO `sys_menu` VALUES ('82', '78', '删除网站设置', '', '', '1', '4', 'website:website:remove', '_self', null, '0', '0', '2021-06-06 02:17:34', '2021-06-06 02:17:34');
INSERT INTO `sys_menu` VALUES ('83', '0', '营销助手', 'layui-icon layui-icon-senior', '', '0', '5', '', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:54:20');
INSERT INTO `sys_menu` VALUES ('84', '83', '优惠券管理', '', 'settings/coupon', '0', '2', 'settings:coupon:view', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:55:51');
INSERT INTO `sys_menu` VALUES ('85', '84', '查询优惠券', '', '', '1', '1', 'settings:coupon:list', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46');
INSERT INTO `sys_menu` VALUES ('86', '84', '添加优惠券', '', '', '1', '2', 'settings:coupon:save', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46');
INSERT INTO `sys_menu` VALUES ('87', '84', '修改优惠券', '', '', '1', '3', 'settings:coupon:update', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46');
INSERT INTO `sys_menu` VALUES ('88', '84', '删除优惠券', '', '', '1', '4', 'settings:coupon:remove', '_self', null, '0', '0', '2021-06-23 07:44:46', '2021-06-23 07:44:46');
INSERT INTO `sys_menu` VALUES ('89', '83', '添加优惠券', '', 'settings/coupon/add', '0', '1', 'settings:coupon:view', '_self', null, '0', '0', '2021-06-23 07:55:44', '2021-06-23 07:56:13');
INSERT INTO `sys_menu` VALUES ('90', '0', 'theme', 'layui-icon layui-icon-senior', '', '0', '1', '', '_self', null, '0', '1', '2021-06-28 00:37:42', '2021-06-28 00:39:46');
INSERT INTO `sys_menu` VALUES ('91', '57', '主题配置', '', 'theme/theme', '0', '3', 'theme:theme:view', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:39:43');
INSERT INTO `sys_menu` VALUES ('92', '91', '查询主题配置', '', '', '1', '1', 'theme:theme:list', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42');
INSERT INTO `sys_menu` VALUES ('93', '91', '添加主题配置', '', '', '1', '2', 'theme:theme:save', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42');
INSERT INTO `sys_menu` VALUES ('94', '91', '修改主题配置', '', '', '1', '3', 'theme:theme:update', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42');
INSERT INTO `sys_menu` VALUES ('95', '91', '删除主题配置', '', '', '1', '4', 'theme:theme:remove', '_self', null, '0', '0', '2021-06-28 00:37:42', '2021-06-28 00:37:42');
INSERT INTO `sys_menu` VALUES ('96', '0', 'settings', 'layui-icon layui-icon-senior', '', '0', '1', '', '_self', null, '0', '1', '2021-07-04 03:59:22', '2021-07-04 04:00:42');
INSERT INTO `sys_menu` VALUES ('97', '57', '商店设置', '', 'settings/shopSettings', '0', '1', 'settings:shopSettings:view', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 04:01:37');
INSERT INTO `sys_menu` VALUES ('98', '97', '查询商店设置', '', '', '1', '1', 'settings:shopSettings:list', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22');
INSERT INTO `sys_menu` VALUES ('99', '97', '添加商店设置', '', '', '1', '2', 'settings:shopSettings:save', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22');
INSERT INTO `sys_menu` VALUES ('100', '97', '修改商店设置', '', '', '1', '3', 'settings:shopSettings:update', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22');
INSERT INTO `sys_menu` VALUES ('101', '97', '删除商店设置', '', '', '1', '4', 'settings:shopSettings:remove', '_self', null, '0', '0', '2021-07-04 03:59:22', '2021-07-04 03:59:22');
INSERT INTO `sys_menu` VALUES ('102', '57', '微信通知', '', 'wxpusher/send', '0', '2', 'settings:wxpusher:view', '_self', null, '0', '0', '2021-07-04 16:35:26', '2021-07-04 16:35:38');

-- ----------------------------
-- Table structure for sys_oper_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_record`;
CREATE TABLE `sys_oper_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `model` varchar(200) DEFAULT NULL COMMENT '操作模块',
  `description` varchar(200) DEFAULT NULL COMMENT '操作方法',
  `url` varchar(200) DEFAULT NULL COMMENT '请求地址',
  `request_method` varchar(200) DEFAULT NULL COMMENT '请求方式',
  `oper_method` varchar(200) DEFAULT NULL COMMENT '调用方法',
  `param` varchar(2000) DEFAULT NULL COMMENT '请求参数',
  `result` varchar(2000) DEFAULT NULL COMMENT '返回结果',
  `ip` varchar(200) DEFAULT NULL COMMENT 'ip地址',
  `comments` varchar(2000) DEFAULT NULL COMMENT '备注',
  `spend_time` int(11) DEFAULT NULL COMMENT '请求耗时,单位毫秒',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态,0成功,1异常',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `sys_oper_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

-- ----------------------------
-- Table structure for sys_orders
-- ----------------------------
DROP TABLE IF EXISTS `sys_orders`;
CREATE TABLE `sys_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member` varchar(255) DEFAULT NULL COMMENT '订单号',
  `status` int(1) DEFAULT NULL COMMENT '状态',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `contact` varchar(255) DEFAULT NULL COMMENT '买家联系方式',
  `ip` varchar(255) DEFAULT NULL COMMENT '买家ip',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `number` int(11) DEFAULT NULL COMMENT '订单数量',
  `pay_type` varchar(255) DEFAULT NULL COMMENT '支付类型',
  `guest_id` int(11) DEFAULT NULL COMMENT '支付用户的id（如果有）',
  `device` varchar(255) DEFAULT NULL COMMENT '购买设备',
  `pay_no` varchar(255) DEFAULT NULL COMMENT '流水号',
  `money` decimal(18,2) DEFAULT NULL COMMENT '付款金额',
  `price` decimal(18,2) NOT NULL COMMENT '提交金额',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `cloud_payid` varchar(255) DEFAULT '' COMMENT '云端id',
  `email` varchar(255) DEFAULT NULL COMMENT '邮件通知',
  `is_coupon` int(1) DEFAULT NULL COMMENT '是否使用优惠券',
  `coupon_id` int(11) DEFAULT NULL COMMENT '优惠券id',
  `user_id` int(11) unsigned DEFAULT NULL COMMENT '用户id',
  `password` varchar(255) DEFAULT NULL COMMENT '订单密码',
  PRIMARY KEY (`id`,`price`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
-- Table structure for sys_order_card
-- ----------------------------
DROP TABLE IF EXISTS `sys_order_card`;
CREATE TABLE `sys_order_card` (
  `id` int(200) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `card_id` int(200) DEFAULT NULL,
  `order_id` int(200) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `oc_order_card_ibfk_1` (`order_id`),
  KEY `oc_order_card_ibfk_2` (`card_id`),
  CONSTRAINT `oc_order_card_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `sys_orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `oc_order_card_ibfk_2` FOREIGN KEY (`card_id`) REFERENCES `sys_cards` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单关联卡密表';

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization` (
  `organization_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '机构id',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级id,0是顶级',
  `organization_name` varchar(200) NOT NULL COMMENT '机构名称',
  `organization_full_name` varchar(200) DEFAULT NULL COMMENT '机构全称',
  `organization_code` varchar(100) DEFAULT NULL COMMENT '机构代码',
  `organization_type` int(11) NOT NULL COMMENT '机构类型',
  `leader_id` int(11) DEFAULT NULL COMMENT '负责人id',
  `sort_number` int(11) NOT NULL DEFAULT '1' COMMENT '排序号',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`organization_id`),
  KEY `leader_id` (`leader_id`),
  CONSTRAINT `sys_organization_ibfk_1` FOREIGN KEY (`leader_id`) REFERENCES `sys_user` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='组织机构';

-- ----------------------------
-- Records of sys_organization
-- ----------------------------
INSERT INTO `sys_organization` VALUES ('1', '0', 'XXX公司', 'XXXXXXXXX科技有限公司', null, '3', null, '1', '', '0', '2020-03-15 13:14:55', '2020-03-21 15:12:49');
INSERT INTO `sys_organization` VALUES ('2', '1', '研发部', '研发部', null, '5', null, '2', '', '0', '2020-03-15 13:15:16', '2020-03-16 00:43:09');
INSERT INTO `sys_organization` VALUES ('3', '2', '高教组', '高等教育行业项目组', null, '6', null, '3', '', '0', '2020-03-15 13:15:45', '2020-03-16 00:42:49');
INSERT INTO `sys_organization` VALUES ('4', '2', '政务组', '政务行业项目组', null, '6', null, '4', '', '0', '2020-03-15 13:16:15', '2020-03-16 00:42:54');
INSERT INTO `sys_organization` VALUES ('5', '2', '制造组', '生产制造行业项目组', null, '6', null, '5', '', '0', '2020-03-15 13:16:37', '2020-03-21 15:13:05');
INSERT INTO `sys_organization` VALUES ('6', '2', '仿真组', '虚拟仿真行业项目组', null, '6', null, '6', '', '0', '2020-03-15 13:16:57', '2020-03-16 00:43:03');
INSERT INTO `sys_organization` VALUES ('7', '1', '测试部', '测试部', null, '5', null, '6', '', '0', '2020-03-15 13:17:19', '2020-03-16 00:43:14');
INSERT INTO `sys_organization` VALUES ('8', '1', '设计部', 'UI设计部门', null, '5', null, '7', '', '0', '2020-03-15 13:17:56', '2020-03-16 00:43:18');
INSERT INTO `sys_organization` VALUES ('9', '1', '市场部', '市场部', null, '5', null, '8', '', '0', '2020-03-15 13:18:15', '2020-03-16 00:43:23');

-- ----------------------------
-- Table structure for sys_pays
-- ----------------------------
DROP TABLE IF EXISTS `sys_pays`;
CREATE TABLE `sys_pays` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `driver` varchar(255) DEFAULT NULL COMMENT '驱动',
  `config` longtext COMMENT '配置',
  `comment` varchar(255) DEFAULT NULL COMMENT '说明',
  `enabled` int(1) DEFAULT NULL COMMENT '开关',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COMMENT='支付配置';

-- ----------------------------
-- Records of sys_pays
-- ----------------------------
INSERT INTO `sys_pays` VALUES ('1', '支付宝', 'mqpay_alipay', '{\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', 'V免签 - 支付宝（费率0）（自己搭建）', '0', '2021-03-29 16:16:05', '2021-03-29 16:16:07');
INSERT INTO `sys_pays` VALUES ('2', '微信', 'mqpay_wxpay', '{\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', 'V免签 - 微信 （费率0）（自己搭建）', '0', '2021-03-29 16:17:52', '2021-03-29 16:17:55');
INSERT INTO `sys_pays` VALUES ('5', '支付宝', 'zlianpay_alipay', '{\"pid\":\"xxx\",\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', '值联码支付 - 支付宝 码支付-微信（易支付接口）（费率0） 申请地址 https://code.zlianpay.cn', '0', '2021-05-24 12:00:01', '2021-05-24 12:00:04');
INSERT INTO `sys_pays` VALUES ('6', '微信', 'zlianpay_wxpay', '{\"pid\":\"xxx\",\"notify_url\":\"xxx\",\"create_url\":\"xxx\",\"key\":\"xxx\"}', '值联码支付 - 微信（易支付接口）（费率0） 申请地址 https://code.zlianpay.cn', '0', '2021-05-24 12:00:55', '2021-05-24 12:01:00');
INSERT INTO `sys_pays` VALUES ('7', '微信', 'yungouos_wxpay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', 'YunGouOS - 微信（个人小薇支付-费率低） 申请地址：https://dwz.cn/QQLN87nX', '0', '2021-06-06 04:53:12', '2021-06-06 04:53:20');
INSERT INTO `sys_pays` VALUES ('8', '支付宝', 'yungouos_alipay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', 'YunGouOS - 支付宝 （个人小薇支付-费率低） 申请地址：https://dwz.cn/QQLN87nX', '0', '2021-06-06 04:54:03', '2021-06-06 04:54:06');
INSERT INTO `sys_pays` VALUES ('9', '微信', 'xunhupay_wxpay', '{\"appid\":\"xxx\",\"appsecret\":\"xxx\",\"notify_url\":\"xxx\"}', '虎皮椒V3 - 微信（费率-H5版2%/普通版1%） 申请地址：https://www.xunhupay.com', '0', '2021-06-06 22:24:47', '2021-06-06 22:24:50');
INSERT INTO `sys_pays` VALUES ('10', '支付宝', 'xunhupay_alipay', '{\"appid\":\"xxx\",\"appsecret\":\"xxx\",\"notify_url\":\"xxx\"}', '虎皮椒V3 - 支付宝 申请地址：https://www.xunhupay.com', '0', '2021-06-06 22:25:19', '2021-06-06 22:25:22');
INSERT INTO `sys_pays` VALUES ('11', '微信', 'jiepay_wxpay', '{\"appid\":\"xxx\",\"apptoken\":\"xxx\"}', '捷支付 - 微信（费率0）申请地址：http://jpay.hmy3.com', '0', '2021-06-07 00:49:23', '2021-06-07 00:49:26');
INSERT INTO `sys_pays` VALUES ('12', '支付宝', 'jiepay_alipay', '{\"appid\":\"xxx\",\"apptoken\":\"xxx\"}', '捷支付 - 支付宝（费率0）申请地址：http://jpay.hmy3.com', '0', '2021-06-07 00:50:05', '2021-06-07 00:50:08');
INSERT INTO `sys_pays` VALUES ('13', '微信', 'payjs_wxpay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', 'Payjs - 微信扫码 申请地址：https://payjs.cn', '0', '2021-06-27 14:17:54', '2021-06-27 14:17:58');
INSERT INTO `sys_pays` VALUES ('14', '支付宝', 'payjs_alipay', '{\"mchId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', 'Payjs - 支付宝扫码 申请地址：https://payjs.cn', '0', '2021-06-27 14:18:38', '2021-06-27 14:18:43');
INSERT INTO `sys_pays` VALUES ('15', '微信', 'yunfu_wxpay', '{\"appid\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', '云付天下 - 微信扫码 申请地址：https://yunfu.hmy3.com', '0', '2021-06-27 20:11:20', '2021-06-27 20:11:24');
INSERT INTO `sys_pays` VALUES ('16', '支付宝', 'yunfu_alipay', '{\"appid\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', '云付天下 - 支付宝扫码 （支付宝最低付款金额10元）申请地址：https://yunfu.hmy3.com', '0', '2021-06-27 20:11:58', '2021-06-27 20:12:01');
INSERT INTO `sys_pays` VALUES ('17', '微信', 'wxpay', '{\"mchId\":\"xxx\",\"appId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', '官方微信 - 扫码支付', '0', '2021-07-02 02:47:37', '2021-07-02 02:47:40');
INSERT INTO `sys_pays` VALUES ('18', '支付宝', 'alipay', '{\"private_key\":\"xxx\",\"notify_url\":\"xxx\",\"app_id\":\"xxx\",\"alipay_public_key\":\"xxx\"}', '官方支付宝 - 当面付', '0', '2021-07-03 18:53:08', '2021-07-02 18:53:11');
INSERT INTO `sys_pays` VALUES ('19', '微信H5', 'wxpay_h5', '{\"mchId\":\"xxx\",\"appId\":\"xxx\",\"notify_url\":\"xxx\",\"key\":\"xxx\"}', '官方微信 - H5支付 （开启后只在手机端显示）', '0', '2021-07-02 23:20:54', '2021-08-17 23:20:56');
INSERT INTO `sys_pays` VALUES ('20', 'Paypal', 'paypal', '{\"clientId\":\"Ae0RQgnQqXmw76ekdromBMy_vP_FOZgB-Bn6U36PeTNki4Dj6guqbS990FHVJjw8i4fI-cZG7YA_S0ob\",\"return_url\":\"xxx\",\"clientSecret\":\"EBsp424xuEUBE-VhQFKM7cjpBiAQDHZsORObuQlAOW0xP1iwasn6-CWEEclmk5__1tN71pEMR5ILG220\"}', 'Paypal 境外支付（默认美元交易）', '0', '2021-08-24 12:04:25', '2021-08-24 12:04:28');

-- ----------------------------
-- Table structure for sys_products
-- ----------------------------
DROP TABLE IF EXISTS `sys_products`;
CREATE TABLE `sys_products` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `price` decimal(18,2) DEFAULT NULL COMMENT '商品金额',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `link` varchar(255) DEFAULT NULL COMMENT '商品链接',
  `status` int(1) DEFAULT NULL COMMENT '商品状态',
  `pd_info` longtext COMMENT '商品详情',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted_at` datetime DEFAULT NULL COMMENT '删除时间',
  `classify_id` int(11) DEFAULT NULL COMMENT '分类id',
  `index_logo` longtext COMMENT '首页截图',
  `image_logo` longtext COMMENT '商品logo',
  `is_wholesale` int(1) DEFAULT NULL COMMENT '批发功能',
  `wholesale` longtext COMMENT '批发配置',
  `restricts` int(11) DEFAULT '0' COMMENT '限制购买',
  `ship_type` int(1) DEFAULT NULL COMMENT '发货类型（0-自动，1-手动）',
  `inventory` int(32) DEFAULT NULL COMMENT '商品库存（人工发货类型生效）',
  `sales` int(32) DEFAULT NULL COMMENT '销量',
  `is_password` int(1) DEFAULT NULL COMMENT '是否开启密码查询',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(200) NOT NULL COMMENT '角色名称',
  `role_code` varchar(200) DEFAULT NULL COMMENT '角色标识',
  `comments` varchar(400) DEFAULT NULL COMMENT '备注',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', 'admin', '管理员', '0', '2020-02-26 15:18:37', '2020-03-21 15:15:54');
INSERT INTO `sys_role` VALUES ('2', '普通用户', 'user', '普通用户', '0', '2020-02-26 15:18:52', '2020-03-21 15:16:02');
INSERT INTO `sys_role` VALUES ('3', '游客', 'guest', '游客', '0', '2020-02-26 15:19:49', '2020-03-21 15:16:57');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_role_permission_role` (`role_id`),
  KEY `menu_id` (`menu_id`),
  CONSTRAINT `sys_role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`menu_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=544 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='角色权限';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('445', '1', '75', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('446', '1', '37', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('447', '1', '38', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('448', '1', '39', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('449', '1', '40', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('450', '1', '41', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('451', '1', '42', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('452', '1', '76', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('453', '1', '43', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('454', '1', '44', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('455', '1', '45', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('456', '1', '46', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('457', '1', '47', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('458', '1', '48', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('459', '1', '54', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('460', '1', '51', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('461', '1', '49', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('462', '1', '50', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('463', '1', '52', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('464', '1', '53', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('465', '1', '55', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('466', '1', '56', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('467', '1', '63', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('468', '1', '64', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('469', '1', '65', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('470', '1', '66', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('471', '1', '67', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('472', '1', '68', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('473', '1', '83', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('474', '1', '89', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('475', '1', '84', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('476', '1', '85', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('477', '1', '86', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('478', '1', '87', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('479', '1', '88', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('480', '1', '57', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('481', '1', '97', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('482', '1', '98', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('483', '1', '99', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('484', '1', '100', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('485', '1', '101', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('486', '1', '58', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('487', '1', '59', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('488', '1', '60', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('489', '1', '61', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('490', '1', '62', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('491', '1', '102', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('492', '1', '91', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('493', '1', '92', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('494', '1', '93', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('495', '1', '94', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('496', '1', '95', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('497', '1', '78', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('498', '1', '79', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('499', '1', '80', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('500', '1', '81', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('501', '1', '82', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('502', '1', '1', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('503', '1', '2', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('504', '1', '3', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('505', '1', '4', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('506', '1', '5', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('507', '1', '6', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('508', '1', '7', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('509', '1', '8', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('510', '1', '9', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('511', '1', '10', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('512', '1', '11', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('513', '1', '12', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('514', '1', '13', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('515', '1', '14', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('516', '1', '15', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('517', '1', '16', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('518', '1', '17', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('519', '1', '18', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('520', '1', '19', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('521', '1', '20', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('522', '1', '21', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('523', '1', '22', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('524', '1', '23', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('525', '1', '24', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('526', '1', '25', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('527', '1', '26', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('528', '1', '27', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('529', '1', '28', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('530', '1', '29', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('531', '1', '30', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('532', '1', '31', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('533', '1', '32', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('534', '1', '33', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('535', '1', '34', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('536', '1', '35', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('537', '1', '36', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('538', '1', '69', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('539', '1', '70', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('540', '1', '71', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('541', '1', '72', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('542', '1', '73', '2021-07-04 16:35:51', '2021-07-04 16:35:51');
INSERT INTO `sys_role_menu` VALUES ('543', '1', '74', '2021-07-04 16:35:51', '2021-07-04 16:35:51');

-- ----------------------------
-- Table structure for sys_shop_settings
-- ----------------------------
DROP TABLE IF EXISTS `sys_shop_settings`;
CREATE TABLE `sys_shop_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `is_window` int(1) DEFAULT NULL COMMENT '是否开启弹窗',
  `window_text` longtext COMMENT '弹窗内容',
  `is_background` varchar(255) DEFAULT NULL COMMENT '全局背景图',
  `store_details` longtext COMMENT '商店详情',
  `is_wxpusher` int(1) DEFAULT NULL COMMENT '是否开启微信通知',
  `app_token` varchar(255) DEFAULT NULL COMMENT 'pusher token',
  `wxpush_uid` varchar(255) DEFAULT NULL COMMENT '微信通知uid',
  `is_email` int(1) DEFAULT '0' COMMENT '邮件通知开关',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='商店设置';

-- ----------------------------
-- Records of sys_shop_settings
-- ----------------------------
INSERT INTO `sys_shop_settings` VALUES ('1', '1', '<div class=\"index-module_textWrap_3ygOc\">\n<p><span class=\"bjh-p\">&nbsp; &nbsp; 想把世界最好的给你，却发现世上最好的是你；我不要不老的青春，只要一个盗不走的爱人。</span></p>\n<p>&nbsp;</p>\n</div>\n<div class=\"index-module_textWrap_3ygOc\">\n<p><span class=\"bjh-p\">&nbsp; &nbsp; 有时幸福就像手心里的沙，握得越紧，失去得越快；有时幸福就像隔岸的花朵，隐约可见，却无法触摸。两个人的世界里，总要一个闹着、一个笑着、一个吵着、一个哄着。</span></p>\n</div>', '/file/20210819/4c29606a4c76462c8f1140ddba3d277e.jpeg', '<p><span style=\"font-size: 12pt; color: #3598db;\">?开源系统 值联云卡</span></p>\n<p><span style=\"font-size: 12pt;\">支持 自动 手动发货模式、支持商品限购、以及商品批发模式、还有营销助手（优惠券系统）</span></p>\n<p><span style=\"font-size: 12pt;\">支持支付渠道：</span>v免签、<span style=\"color: #e03e2d;\">值联支付</span>、YunGouOS、码支付、虎皮椒支付、捷支付、payjs、<span style=\"color: #e03e2d;\">云付天下</span>&nbsp;、<strong><span style=\"color: #236fa1;\">微信官方扫码支付<span style=\"color: #e03e2d;\">（新增）</span></span></strong><span style=\"color: #236fa1;\">、<span style=\"color: #2a3fff;\">支付宝当面付（新增）</span></span></p>\n<p><span style=\"font-size: 12pt;\">?? 新增支持 （<span style=\"color: #2dc26b;\">白昼模式、暗黑模式</span>）模板</span></p>\n<p><span style=\"font-size: 12pt;\">基于 ☕ <span style=\"color: #e03e2d;\">Java</span> 语言开发 使用 <span style=\"color: #e03e2d;\">Spring boot</span> 框架构建、安全高效</span></p>\n<p><span style=\"font-size: 12pt;\">开源地址：<span style=\"color: #e03e2d;\"><a style=\"color: #e03e2d;\" title=\"打开项目开源地址\" href=\"https://github.com/Panyoujies/zlianpay-faka\">打开项目开源地址</a></span> 支持开源项目给个 <span style=\"color: #1c68ff;\">Star</span> 呗 非常感谢</span></p>\n<p><span style=\"font-size: 12pt;\">部署教程：<a title=\"打开查看部署教程\" href=\"http://doc.zlianpay.cn/index.php/archives/29/\" target=\"_blank\" rel=\"noopener\"><span style=\"color: #e03e2d;\">打开查看部署教程</span></a></span></p>\n<p><span style=\"font-size: 12pt;\">如您有任何疑问请联系Q群：940086807</span></p>', '0', 'AT_Wn4nO8FWdLZuLHo2iiwrqkXQ6iQ6NlHt', 'UID_YOKIVya0Pv1lHoPpzFvofUQqQqEx', '1');

-- ----------------------------
-- Table structure for sys_theme
-- ----------------------------
DROP TABLE IF EXISTS `sys_theme`;
CREATE TABLE `sys_theme` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) DEFAULT NULL COMMENT '主题名称',
  `description` longtext COMMENT '说明',
  `driver` varchar(255) DEFAULT NULL COMMENT '主题驱动',
  `enable` int(1) DEFAULT NULL COMMENT '是否设置',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='主题配置';

-- ----------------------------
-- Records of sys_theme
-- ----------------------------
INSERT INTO `sys_theme` VALUES ('1', '白昼模式', '白色的主题', 'bright', '1', '2021-06-28 00:58:32', '2021-06-28 00:58:34');
INSERT INTO `sys_theme` VALUES ('2', '暗黑模式', '黑夜的主题', 'night', '0', '2021-06-28 00:59:05', '2021-06-28 00:59:08');
INSERT INTO `sys_theme` VALUES ('3', '炫丽暗黑', '炫丽的暗黑模式', 'easy-night', '0', '2021-07-03 23:56:39', '2021-07-03 23:56:42');
INSERT INTO `sys_theme` VALUES ('4', '炫丽白昼', '炫丽的白昼模式', 'easy-bright', '0', '2021-07-20 05:02:20', '2021-07-20 05:02:23');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '账号',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `nick_name` varchar(200) NOT NULL COMMENT '昵称',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `sex` int(11) DEFAULT NULL COMMENT '性别',
  `phone` varchar(200) DEFAULT NULL COMMENT '手机号',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `email_verified` int(1) NOT NULL DEFAULT '0' COMMENT '邮箱是否验证,0否,1是',
  `true_name` varchar(200) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(200) DEFAULT NULL COMMENT '身份证号',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `introduction` varchar(200) DEFAULT NULL COMMENT '个人简介',
  `organization_id` int(11) DEFAULT NULL COMMENT '机构id',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '状态,0正常,1冻结',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0否,1是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `qq_uuid` varchar(255) DEFAULT NULL COMMENT 'qquuid',
  `wx_uuid` varchar(255) DEFAULT NULL COMMENT 'wxuuid',
  PRIMARY KEY (`user_id`),
  KEY `organization_id` (`organization_id`),
  CONSTRAINT `sys_user_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `sys_organization` (`organization_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', null, '1', '', null, '0', null, null, null, '312312344444', null, '0', '0', '2020-01-13 14:43:52', '2021-07-14 09:35:49', null, null);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `FK_sys_user_role` (`user_id`),
  KEY `FK_sys_user_role_role` (`role_id`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='用户角色';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('2', '1', '1', '2021-08-23 18:29:06', '2021-08-23 18:29:06');

-- ----------------------------
-- Table structure for sys_website
-- ----------------------------
DROP TABLE IF EXISTS `sys_website`;
CREATE TABLE `sys_website` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
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
-- Records of sys_website
-- ----------------------------
INSERT INTO `sys_website` VALUES ('1', '值联云卡', 'http://localhost:8080', '20210817/cccb56782c1d45f6a3d978a4a69b369b.png', '1724962375', '打造最美云卡', '发卡,发卡,发卡卡', '值联云卡- 最安全的个人发卡系统', '20210817/64c4cad7f6434bac866a71e285aa2b14.png');
