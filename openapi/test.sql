/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-10-19 19:31:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_test_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_test_user`;
CREATE TABLE `sys_test_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_code` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '用户号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of sys_test_user
-- ----------------------------
INSERT INTO `sys_test_user` VALUES ('1', '123');
INSERT INTO `sys_test_user` VALUES ('2', '23333');

DROP TABLE IF EXISTS `sms_message`;
CREATE TABLE `sms_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msg_no` varchar(30) DEFAULT NULL COMMENT '编号',
  `iphone_no` varchar(50) DEFAULT NULL COMMENT '号码',
  `msg_content` varchar(500) DEFAULT NULL COMMENT '内容',
  `msg_time` varchar(30) DEFAULT NULL COMMENT '发送时间',
  `status` char(2) DEFAULT NULL COMMENT '状态',
  `create_by` varchar(40) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `update_by` varchar(40) DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
