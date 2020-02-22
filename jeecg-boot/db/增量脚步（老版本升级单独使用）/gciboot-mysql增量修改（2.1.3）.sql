-- ----------------------------
-- Table structure for sys_depart行政机构表
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart`;
CREATE TABLE `sys_depart`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父机构ID',
  `depart_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构/部门名称',
  `depart_name_en` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文名',
  `depart_name_abbr` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩写',
  `depart_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `depart_category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '机构类别 1组织机构，2岗位',
  `depart_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构类型 1一级部门 2子部门',
  `depart_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `fax` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传真',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `memo` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（1启用，0不启用）',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_depart_parent_id`(`parent_id`) USING BTREE,
  INDEX `index_depart_depart_order`(`depart_order`) USING BTREE,
  INDEX `index_depart_depart_code`(`depart_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行政机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_platform
-- ----------------------------
DROP TABLE IF EXISTS `sys_platform`;
CREATE TABLE `sys_platform`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `platform_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台名称',
  `platform_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',

  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '平台表'  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_platform
-- ----------------------------
INSERT INTO `sys_platform` VALUES ('0000', '权限管理平台', '0000', '用户权限管理，各个系统的基础平台', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Update of sys_permission，由于增加了表sys_platform因此需要对表sys_permission进行修改
-- ----------------------------
ALTER TABLE `sys_permission`
ADD  `platform_code`  VARCHAR(32) NULL DEFAULT NULL COMMENT '所属平台';
UPDATE sys_permission p SET p.platform_id='0000';


-- ----------------------------
-- Table structure for sys_platform_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_platform_org`;
CREATE TABLE `sys_platform_org`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台id',
  `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_org_groupk_platformid`(`platform_id`) USING BTREE,
  INDEX `index_org_groupk_orgid`(`org_id`) USING BTREE,
  INDEX `index_org_groupk_platformidandorgid`(`platform_id`, `org_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;






-- 初始化基础数据
-- 添加管理员用户
-- 添加平台
-- sys_user 新增一条记录
-- sys_user_role 新增一条记录
-- sys_role_permission 新增N条记录
-- sys_permission 新增N条资源记录
INSERT INTO `sys_user` VALUES ('gcia23d68d884d4ebb19d07889727dae', 'gciadmin', 'gci管理员', 'cb362cfeefbf3d8d', 'RCGTeGiH', 'user/20190119/logo-2_1547868176839.png', '2018-12-05 00:00:00', 1, '11@qq.com', '18566666661', 'A01', 1, 0, 1, '111', '', NULL, NULL, '2038-06-21 17:54:10', 'admin', '2019-11-21 16:39:35');

INSERT INTO `sys_platform` VALUES ('0000', '权限管理平台', '0000', '用户权限管理，各个系统的基础平台', NULL, NULL, NULL, NULL,'gci');

-- 用户所属部门
INSERT INTO `sys_user_org` VALUES ('1197434365652623362', 'e9ca23d68d884d4ebb19d07889727dae', 'c6d7cb4deeac411cb3384b1b31278596');

-- 添加管理员角色
INSERT INTO `sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', NULL, '2018-12-21 18:03:39', 'admin', '2019-05-20 11:40:26',null,null);
-- 添加管理员权限，必须要有系统管理、用户管理、角色管理三个权限
INSERT INTO `sys_role_permission` VALUES ('1209423580355481602', 'f6817f48af4fb3af11b9e8bf182f618b', '190c2b43bec6a5f7a4194a85db67d96a', NULL,null);
INSERT INTO `sys_role_permission` VALUES ('980171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL,null);
INSERT INTO `sys_role_permission` VALUES ('4204f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', NULL,null);

INSERT INTO `sys_role_permission` VALUES ('gci9423580355481602', 'f6817f48af4fb3af11b9e8bf182f618b', '1231170591027445762', NULL,null);
INSERT INTO `sys_role_permission` VALUES ('gci171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', '1231169033288105986', NULL,null);
INSERT INTO `sys_role_permission` VALUES ('gci4f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '1231170454372827137', NULL,null);



-- 添加租户标示
alter table sys_platform_org add tenant_id varchar(32) NULL DEFAULT NULL COMMENT '租户ID';
alter table sys_fill_rule add tenant_id varchar(32) NULL DEFAULT NULL COMMENT '租户ID';
alter table sys_dict_item add tenant_id varchar(32) NULL DEFAULT NULL COMMENT '租户ID';


alter table sys_announcement add tenant_id varchar(32) NULL DEFAULT NULL COMMENT '租户ID';
alter table sys_announcement_send add tenant_id varchar(32) NULL DEFAULT NULL COMMENT '租户ID';
alter table sys_user_role add tenant_id varchar(32) NULL DEFAULT NULL COMMENT '租户ID';



