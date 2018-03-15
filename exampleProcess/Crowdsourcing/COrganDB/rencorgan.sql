/*
Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : rencorgan

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-03-16 00:37:14
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
INSERT INTO `ren_agent` VALUES ('Agent_39e499e1-25a3-11e8-91b1-2c4d54f01cf2', 'AutomicQueryAgent', 'http://localhost:10300/', '0', '');
INSERT INTO `ren_agent` VALUES ('Agent_ba525af0-24da-11e8-ae89-2c4d54f01cf2', 'AutoMergeAgent', 'http://localhost:10300/', '0', '');

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
INSERT INTO `ren_capability` VALUES ('Capa_1f54376e-25a3-11e8-8267-2c4d54f01cf2', 'CanQuery', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_cb8b61e1-24da-11e8-a3d8-2c4d54f01cf2', 'CanMerge', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_cedf6e11-250a-11e8-b05a-2c4d54f01cf2', 'CanPublish', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2', 'CanSolve', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_d3e7e29e-24da-11e8-8487-2c4d54f01cf2', 'CanSolveVote', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_d72c864f-24da-11e8-b535-2c4d54f01cf2', 'CanDecompose', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_db79994f-24da-11e8-abc0-2c4d54f01cf2', 'CanDecomposeVote', '', '');
INSERT INTO `ren_capability` VALUES ('Capa_e986668f-24da-11e8-972c-2c4d54f01cf2', 'CanJudge', '', '');

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
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_connect
-- ----------------------------
INSERT INTO `ren_connect` VALUES ('32', 'Human_7cbeffd1-24db-11e8-897c-2c4d54f01cf2', 'Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('33', 'Human_7cbeffd1-24db-11e8-897c-2c4d54f01cf2', 'Capa_db79994f-24da-11e8-abc0-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('34', 'Human_7cbeffd1-24db-11e8-897c-2c4d54f01cf2', 'Capa_e986668f-24da-11e8-972c-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('35', 'Human_868c2791-24db-11e8-af0e-2c4d54f01cf2', 'Capa_d3e7e29e-24da-11e8-8487-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('36', 'Human_868c2791-24db-11e8-af0e-2c4d54f01cf2', 'Capa_d72c864f-24da-11e8-b535-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('37', 'Human_868c2791-24db-11e8-af0e-2c4d54f01cf2', 'Capa_e986668f-24da-11e8-972c-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('38', 'Human_952a384f-24db-11e8-89cc-2c4d54f01cf2', 'Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('39', 'Human_952a384f-24db-11e8-89cc-2c4d54f01cf2', 'Capa_d72c864f-24da-11e8-b535-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('40', 'Human_952a384f-24db-11e8-89cc-2c4d54f01cf2', 'Capa_e986668f-24da-11e8-972c-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('41', 'Human_a21e69a1-24db-11e8-b5d4-2c4d54f01cf2', 'Capa_d3e7e29e-24da-11e8-8487-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('42', 'Human_a21e69a1-24db-11e8-b5d4-2c4d54f01cf2', 'Capa_db79994f-24da-11e8-abc0-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('43', 'Human_a21e69a1-24db-11e8-b5d4-2c4d54f01cf2', 'Capa_e986668f-24da-11e8-972c-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('44', 'Human_bc6e409e-24db-11e8-a352-2c4d54f01cf2', 'Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('45', 'Human_bc6e409e-24db-11e8-a352-2c4d54f01cf2', 'Capa_d72c864f-24da-11e8-b535-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('46', 'Human_bc6e409e-24db-11e8-a352-2c4d54f01cf2', 'Capa_db79994f-24da-11e8-abc0-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('47', 'Human_e4810aa1-24db-11e8-8760-2c4d54f01cf2', 'Capa_d3e7e29e-24da-11e8-8487-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('48', 'Human_e4810aa1-24db-11e8-8760-2c4d54f01cf2', 'Capa_db79994f-24da-11e8-abc0-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('49', 'Human_e4810aa1-24db-11e8-8760-2c4d54f01cf2', 'Capa_e986668f-24da-11e8-972c-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('50', 'Human_ed89a9de-24db-11e8-88bf-2c4d54f01cf2', 'Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('51', 'Human_ed89a9de-24db-11e8-88bf-2c4d54f01cf2', 'Capa_d72c864f-24da-11e8-b535-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('52', 'Human_fece2280-24db-11e8-b31d-2c4d54f01cf2', 'Capa_d3e7e29e-24da-11e8-8487-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('53', 'Human_fece2280-24db-11e8-b31d-2c4d54f01cf2', 'Capa_db79994f-24da-11e8-abc0-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('54', 'Human_10cc2721-24dc-11e8-b995-2c4d54f01cf2', 'Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('55', 'Human_10cc2721-24dc-11e8-b995-2c4d54f01cf2', 'Capa_e986668f-24da-11e8-972c-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('56', 'Human_22127480-24dc-11e8-8a9b-2c4d54f01cf2', 'Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('57', 'Human_22127480-24dc-11e8-8a9b-2c4d54f01cf2', 'Capa_d72c864f-24da-11e8-b535-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('60', 'Human_c8ed5d9e-250a-11e8-9c5e-2c4d54f01cf2', 'Capa_cedf6e11-250a-11e8-b05a-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('62', 'Agent_39e499e1-25a3-11e8-91b1-2c4d54f01cf2', 'Capa_1f54376e-25a3-11e8-8267-2c4d54f01cf2');
INSERT INTO `ren_connect` VALUES ('64', 'Agent_ba525af0-24da-11e8-ae89-2c4d54f01cf2', 'Capa_cb8b61e1-24da-11e8-a3d8-2c4d54f01cf2');

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
INSERT INTO `ren_human` VALUES ('Human_10cc2721-24dc-11e8-b995-2c4d54f01cf2', 'Iris', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_22127480-24dc-11e8-8a9b-2c4d54f01cf2', 'Jesmine', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_7cbeffd1-24db-11e8-897c-2c4d54f01cf2', 'Alice', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_868c2791-24db-11e8-af0e-2c4d54f01cf2', 'Bob', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_952a384f-24db-11e8-89cc-2c4d54f01cf2', 'Cynthia', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_a21e69a1-24db-11e8-b5d4-2c4d54f01cf2', 'Darling', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_bc6e409e-24db-11e8-a352-2c4d54f01cf2', 'Erinne', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_c8ed5d9e-250a-11e8-9c5e-2c4d54f01cf2', 'Publisher', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_e4810aa1-24db-11e8-8760-2c4d54f01cf2', 'Finne', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_ed89a9de-24db-11e8-88bf-2c4d54f01cf2', 'Gear', '', '', '');
INSERT INTO `ren_human` VALUES ('Human_fece2280-24db-11e8-b31d-2c4d54f01cf2', 'Hyacinth', '', '', '');

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
