/*
Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : rencorgan

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-03-16 00:49:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ren_agent`
-- ----------------------------
DROP TABLE IF EXISTS `ren_agent`;
CREATE TABLE `ren_agent` (
  `id` varchar(60) NOT NULL,
  `name` text,
  `location` text,
  `type` int(11) DEFAULT NULL,
  `note` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_agent
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_capability`
-- ----------------------------
DROP TABLE IF EXISTS `ren_capability`;
CREATE TABLE `ren_capability` (
  `id` varchar(64) NOT NULL,
  `name` text,
  `description` text,
  `note` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_capability
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_cconfig`
-- ----------------------------
DROP TABLE IF EXISTS `ren_cconfig`;
CREATE TABLE `ren_cconfig` (
  `rkey` varchar(64) NOT NULL,
  `rvalue` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`rkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_cconfig
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_connect`
-- ----------------------------
DROP TABLE IF EXISTS `ren_connect`;
CREATE TABLE `ren_connect` (
  `conId` int(11) NOT NULL AUTO_INCREMENT,
  `workerId` varchar(64) DEFAULT NULL,
  `belongToOrganizableId` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`conId`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_connect
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_group`
-- ----------------------------
DROP TABLE IF EXISTS `ren_group`;
CREATE TABLE `ren_group` (
  `id` varchar(64) NOT NULL,
  `name` text,
  `description` text,
  `note` text,
  `belongToId` varchar(64) DEFAULT NULL,
  `groupType` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_group
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_human`
-- ----------------------------
DROP TABLE IF EXISTS `ren_human`;
CREATE TABLE `ren_human` (
  `id` varchar(64) NOT NULL,
  `person_id` text,
  `firstname` text,
  `lastname` text,
  `note` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_human
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_log`
-- ----------------------------
DROP TABLE IF EXISTS `ren_log`;
CREATE TABLE `ren_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(64) DEFAULT NULL,
  `level` varchar(16) DEFAULT NULL,
  `message` text,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_log
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_position`
-- ----------------------------
DROP TABLE IF EXISTS `ren_position`;
CREATE TABLE `ren_position` (
  `id` varchar(64) NOT NULL,
  `name` text,
  `description` text,
  `note` text,
  `belongToId` varchar(64) DEFAULT NULL,
  `reportToId` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_position
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_user`
-- ----------------------------
DROP TABLE IF EXISTS `ren_user`;
CREATE TABLE `ren_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `password` varchar(128) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `createtimestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_user
-- ----------------------------
INSERT INTO `ren_user` VALUES ('1', 'admin', '57a64f6a47aa58e84035df91c798ac8e89c732b0363dfc01da54b78b7ac015bf', '1', '0', '2018-02-11 22:12:00');
