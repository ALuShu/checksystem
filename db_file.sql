/*
 Navicat Premium Data Transfer

 Source Server         : MySQL80
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : db_file

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 10/01/2020 23:47:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_authority`;
CREATE TABLE `sys_authority`  (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tag` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_authority
-- ----------------------------
INSERT INTO `sys_authority` VALUES (1, '增加用户', 'USER_ADD');
INSERT INTO `sys_authority` VALUES (2, '更新用户', 'USER_UPDATE');
INSERT INTO `sys_authority` VALUES (3, '删除用户', 'USER_DELETE');
INSERT INTO `sys_authority` VALUES (4, '查询用户', 'USER_SELECT');
INSERT INTO `sys_authority` VALUES (5, '上传文件', 'FILE_UPLOAD');
INSERT INTO `sys_authority` VALUES (6, '更新文件', 'FILE_UPDATE');
INSERT INTO `sys_authority` VALUES (7, '删除文件', 'FILE_DELETE');
INSERT INTO `sys_authority` VALUES (8, '搜索文件', 'FILE_SELECT');
INSERT INTO `sys_authority` VALUES (9, '查重', 'FILE_CHECK');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件命名',
  `size` bigint(12) NULL DEFAULT NULL COMMENT '文件大小',
  `update_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  `type` int(2) NOT NULL COMMENT '文件类型，0表示dir或1表示file',
  `permission` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '-所有人-拥有者-管理员：如文件夹：-rw--rw--rw-;\r\n-rwx-rwx-rwx',
  `owner` int(10) NOT NULL COMMENT '拥有者id',
  `status` int(2) NULL DEFAULT NULL COMMENT '作业文件状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `owner_index`(`owner`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_inform
-- ----------------------------
DROP TABLE IF EXISTS `sys_inform`;
CREATE TABLE `sys_inform`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `send_id` int(10) NOT NULL,
  `receive_id` int(10) NOT NULL,
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `recieve_index`(`receive_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(1) NOT NULL AUTO_INCREMENT,
  `name` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `detail` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '超级管理');
INSERT INTO `sys_role` VALUES (2, 'teacher', '教师');
INSERT INTO `sys_role` VALUES (3, 'student', '学生');

-- ----------------------------
-- Table structure for sys_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_authority`;
CREATE TABLE `sys_role_authority`  (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `role_id` int(1) NOT NULL,
  `authority_id` int(2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_authority
-- ----------------------------
INSERT INTO `sys_role_authority` VALUES (1, 1, 1);
INSERT INTO `sys_role_authority` VALUES (2, 1, 2);
INSERT INTO `sys_role_authority` VALUES (3, 1, 3);
INSERT INTO `sys_role_authority` VALUES (4, 1, 4);
INSERT INTO `sys_role_authority` VALUES (5, 2, 5);
INSERT INTO `sys_role_authority` VALUES (6, 2, 6);
INSERT INTO `sys_role_authority` VALUES (7, 2, 7);
INSERT INTO `sys_role_authority` VALUES (8, 2, 8);
INSERT INTO `sys_role_authority` VALUES (9, 2, 9);
INSERT INTO `sys_role_authority` VALUES (10, 3, 4);
INSERT INTO `sys_role_authority` VALUES (11, 3, 5);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户id，唯一',
  `username` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号，常为学号或工号，管理员为root',
  `password` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码，限制6到15位',
  `realname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户真实姓名',
  `department` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生和教师的系别',
  `major` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生的专业',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `account_non_expired` tinyint(1) NOT NULL COMMENT '账户是否过期',
  `account_non_locked` tinyint(1) NOT NULL COMMENT '账户是否锁定',
  `credentials_non_expired` tinyint(1) NOT NULL COMMENT '证书是否过期',
  `enabled` tinyint(1) NOT NULL COMMENT '账户是否可用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_index`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'root', 'root', '系统管理员', '', NULL, '2020-01-10 11:46:20', '2020-01-10 11:46:20', 1, 1, 1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `role_id` int(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
