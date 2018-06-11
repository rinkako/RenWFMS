/*
Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : rencorgan

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-11 17:21:34
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
INSERT INTO `ren_agent` VALUES ('Agent_30e35ff0-1263-11e8-9c46-5404a6a99e5d', 'CasherPrinterAgent', 'http://localhost:10300/agent/casherPrinterAgent', '0', '');
INSERT INTO `ren_agent` VALUES ('Agent_4364020f-1263-11e8-a2c0-5404a6a99e5d', 'KitchenPrinterAgent', ' http://localhost:10300/agent/kitchenPrinterAgent ', '1', '');
INSERT INTO `ren_agent` VALUES ('Agent_57dff470-6a46-11e8-9b6b-2c4d54f01cf2', 'RSTestAgent', ' http://localhost:10301/agent/testAgent', '0', '');

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
INSERT INTO `ren_capability` VALUES ('Capa_61fc4da1-6a46-11e8-b0d1-2c4d54f01cf2', 'canTestSpeed', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_6e162f0f-1263-11e8-ad80-5404a6a99e5d', 'canPrint', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_9d027230-1235-11e8-b79f-5404a6a99e5d', 'cooking', 'can be a cook', '');

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
INSERT INTO `ren_cconfig` VALUES ('organizationId', 'COrg_571d200f-0f35-11e8-9072-5404a6a99e5d');

-- ----------------------------
-- Table structure for `ren_connect`
-- ----------------------------
DROP TABLE IF EXISTS `ren_connect`;
CREATE TABLE `ren_connect` (
  `conId` int(11) NOT NULL AUTO_INCREMENT,
  `workerId` varchar(64) DEFAULT NULL,
  `belongToOrganizableId` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`conId`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_connect
-- ----------------------------
INSERT INTO `ren_connect` VALUES ('1', 'Human_88a4f9c0-1235-11e8-b3fc-5404a6a99e5d', 'Dept_1dd7b680-1250-11e8-902d-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('2', 'Human_88a4f9c0-1235-11e8-b3fc-5404a6a99e5d', 'Pos_23e36970-1250-11e8-a08a-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('3', 'Human_fb10f3c0-1261-11e8-9bd4-5404a6a99e5d', 'Capa_9d027230-1235-11e8-b79f-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('4', 'Human_fb10f3c0-1261-11e8-9bd4-5404a6a99e5d', 'Dept_9898d7b0-124f-11e8-a5f8-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('5', 'Human_fb10f3c0-1261-11e8-9bd4-5404a6a99e5d', 'Pos_113f60cf-1250-11e8-bc6c-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('6', 'Human_0cb391ee-1262-11e8-88c3-5404a6a99e5d', 'Capa_9d027230-1235-11e8-b79f-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('7', 'Human_0cb391ee-1262-11e8-88c3-5404a6a99e5d', 'Dept_9898d7b0-124f-11e8-a5f8-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('8', 'Human_0cb391ee-1262-11e8-88c3-5404a6a99e5d', 'Pos_113f60cf-1250-11e8-bc6c-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('9', 'Human_1da66821-1262-11e8-aa99-5404a6a99e5d', 'Capa_9d027230-1235-11e8-b79f-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('10', 'Human_1da66821-1262-11e8-aa99-5404a6a99e5d', 'Dept_9898d7b0-124f-11e8-a5f8-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('11', 'Human_1da66821-1262-11e8-aa99-5404a6a99e5d', 'Pos_113f60cf-1250-11e8-bc6c-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('12', 'Human_2fb2285e-1262-11e8-9d01-5404a6a99e5d', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('13', 'Human_2fb2285e-1262-11e8-9d01-5404a6a99e5d', 'Pos_7a52f1e1-1250-11e8-a2b2-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('14', 'Human_4130c3cf-1262-11e8-b55a-5404a6a99e5d', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('15', 'Human_4130c3cf-1262-11e8-b55a-5404a6a99e5d', 'Pos_7a52f1e1-1250-11e8-a2b2-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('16', 'Human_54d28d61-1262-11e8-b330-5404a6a99e5d', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('17', 'Human_54d28d61-1262-11e8-b330-5404a6a99e5d', 'Pos_7a52f1e1-1250-11e8-a2b2-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('18', 'Human_8c9d8ab0-1262-11e8-b057-5404a6a99e5d', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('19', 'Human_8c9d8ab0-1262-11e8-b057-5404a6a99e5d', 'Pos_62fc369e-1250-11e8-bf95-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('20', 'Human_abf49cf0-1262-11e8-845b-5404a6a99e5d', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('21', 'Human_abf49cf0-1262-11e8-845b-5404a6a99e5d', 'Pos_62fc369e-1250-11e8-bf95-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('22', 'Human_abf49cf0-1262-11e8-845b-5404a6a99e5d', 'Pos_7a52f1e1-1250-11e8-a2b2-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('27', 'Agent_30e35ff0-1263-11e8-9c46-5404a6a99e5d', 'Capa_6e162f0f-1263-11e8-ad80-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('28', 'Agent_30e35ff0-1263-11e8-9c46-5404a6a99e5d', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('29', 'Agent_4364020f-1263-11e8-a2c0-5404a6a99e5d', 'Capa_6e162f0f-1263-11e8-ad80-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('30', 'Agent_4364020f-1263-11e8-a2c0-5404a6a99e5d', 'Dept_9898d7b0-124f-11e8-a5f8-5404a6a99e5d');
INSERT INTO `ren_connect` VALUES ('32', 'Agent_57dff470-6a46-11e8-9b6b-2c4d54f01cf2', 'Capa_61fc4da1-6a46-11e8-b0d1-2c4d54f01cf2');

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
INSERT INTO `ren_group` VALUES ('Dept_1dd7b680-1250-11e8-902d-5404a6a99e5d', 'ManagerGroup', '', '', '', '2');
INSERT INTO `ren_group` VALUES ('Dept_9898d7b0-124f-11e8-a5f8-5404a6a99e5d', 'KitchenDept', '', '', '', '0');
INSERT INTO `ren_group` VALUES ('Dept_a3246d70-124f-11e8-b430-5404a6a99e5d', 'WaiterDept', '', '', '', '0');

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
INSERT INTO `ren_human` VALUES ('Human_0cb391ee-1262-11e8-88c3-5404a6a99e5d', '20002', 'cookman2', 'Levy', 'age:32;sex:male');
INSERT INTO `ren_human` VALUES ('Human_1da66821-1262-11e8-aa99-5404a6a99e5d', '20003', 'cookman3', 'Frank', 'age:19;sex:female');
INSERT INTO `ren_human` VALUES ('Human_2fb2285e-1262-11e8-9d01-5404a6a99e5d', '30001', 'Waiterman1', 'Annie', 'age:17;sex:female');
INSERT INTO `ren_human` VALUES ('Human_4130c3cf-1262-11e8-b55a-5404a6a99e5d', '30002', 'Waiterman2', 'Alice', 'age:25;sex:female');
INSERT INTO `ren_human` VALUES ('Human_54d28d61-1262-11e8-b330-5404a6a99e5d', '30003', 'Waiterman3', 'Joe', 'age:33;sex:male');
INSERT INTO `ren_human` VALUES ('Human_88a4f9c0-1235-11e8-b3fc-5404a6a99e5d', '10001', 'Bob', 'Smith', 'age:30;sex:male');
INSERT INTO `ren_human` VALUES ('Human_8c9d8ab0-1262-11e8-b057-5404a6a99e5d', '40001', 'Casher1', 'Ann', 'age:22;sex:female');
INSERT INTO `ren_human` VALUES ('Human_abf49cf0-1262-11e8-845b-5404a6a99e5d', '40002', 'CasherWaiter1', 'Sapphire', 'age:23;sex:female');
INSERT INTO `ren_human` VALUES ('Human_fb10f3c0-1261-11e8-9bd4-5404a6a99e5d', '20001', 'cookman1', 'John', 'age:22;sex:male ');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_log
-- ----------------------------
INSERT INTO `ren_log` VALUES ('1', 'CController', 'Warning', 'username:, session:541fd8a1-1235-11e8-a1cc-5404a6a99e5d_admin unauthorized request.', '2018-02-15 17:59:55');
INSERT INTO `ren_log` VALUES ('2', 'CController', 'Warning', 'username:, session:541fd8a1-1235-11e8-a1cc-5404a6a99e5d_admin unauthorized request.', '2018-02-15 17:59:56');
INSERT INTO `ren_log` VALUES ('3', 'CController', 'Warning', 'username:, session:0104c06e-1237-11e8-b67b-5404a6a99e5d_admin unauthorized request.', '2018-02-15 18:03:37');
INSERT INTO `ren_log` VALUES ('4', 'CController', 'Warning', 'username:, session:1ba5629e-124f-11e8-8da3-5404a6a99e5d_admin unauthorized request.', '2018-02-15 20:55:32');

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
INSERT INTO `ren_position` VALUES ('Pos_113f60cf-1250-11e8-bc6c-5404a6a99e5d', 'cook', '', '', 'Dept_9898d7b0-124f-11e8-a5f8-5404a6a99e5d', 'Pos_23e36970-1250-11e8-a08a-5404a6a99e5d');
INSERT INTO `ren_position` VALUES ('Pos_23e36970-1250-11e8-a08a-5404a6a99e5d', 'manager', '', '', 'Dept_1dd7b680-1250-11e8-902d-5404a6a99e5d', '');
INSERT INTO `ren_position` VALUES ('Pos_62fc369e-1250-11e8-bf95-5404a6a99e5d', 'casher', '', '', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d', 'Pos_23e36970-1250-11e8-a08a-5404a6a99e5d');
INSERT INTO `ren_position` VALUES ('Pos_7a52f1e1-1250-11e8-a2b2-5404a6a99e5d', 'orderWaiter', '', '', 'Dept_a3246d70-124f-11e8-b430-5404a6a99e5d', 'Pos_23e36970-1250-11e8-a08a-5404a6a99e5d');

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
