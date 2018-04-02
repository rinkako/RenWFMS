/*
Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : rencsdemo

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-04-02 12:14:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ren_decompose`
-- ----------------------------
DROP TABLE IF EXISTS `ren_decompose`;
CREATE TABLE `ren_decompose` (
  `rtid` varchar(64) NOT NULL,
  `nodeId` varchar(64) NOT NULL,
  `workerId` varchar(64) NOT NULL,
  `decompose` longtext,
  `voted` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`rtid`,`nodeId`,`workerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_decompose
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_midsolution`
-- ----------------------------
DROP TABLE IF EXISTS `ren_midsolution`;
CREATE TABLE `ren_midsolution` (
  `rtid` varchar(64) NOT NULL,
  `nodeId` varchar(64) NOT NULL,
  `workerId` varchar(64) NOT NULL,
  `solution` longtext,
  `voted` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`rtid`,`nodeId`,`workerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_midsolution
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_request`
-- ----------------------------
DROP TABLE IF EXISTS `ren_request`;
CREATE TABLE `ren_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `requester` varchar(128) NOT NULL,
  `description` text NOT NULL,
  `status` varchar(64) NOT NULL DEFAULT 'Solving',
  `judgeCount` int(11) NOT NULL,
  `decomposeCount` int(11) NOT NULL,
  `decomposeVoteCount` int(11) NOT NULL,
  `solveCount` int(11) NOT NULL,
  `solveVoteCount` int(11) NOT NULL,
  `solution` longtext,
  `rtid` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_request
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_solution`
-- ----------------------------
DROP TABLE IF EXISTS `ren_solution`;
CREATE TABLE `ren_solution` (
  `supervisor` varchar(64) NOT NULL,
  `ordinal` varchar(64) NOT NULL,
  `solution` longtext,
  PRIMARY KEY (`supervisor`,`ordinal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_solution
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_user`
-- ----------------------------
DROP TABLE IF EXISTS `ren_user`;
CREATE TABLE `ren_user` (
  `username` varchar(64) NOT NULL,
  `password` varchar(128) DEFAULT NULL,
  `level` int(11) DEFAULT '0',
  `workerId` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_user
-- ----------------------------
INSERT INTO `ren_user` VALUES ('admin', '1', '1', null);
INSERT INTO `ren_user` VALUES ('Alice', '1', '0', 'Human_7cbeffd1-24db-11e8-897c-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Bob', '1', '0', 'Human_868c2791-24db-11e8-af0e-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Cynthia', '1', '0', 'Human_952a384f-24db-11e8-89cc-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Darling', '1', '0', 'Human_a21e69a1-24db-11e8-b5d4-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Erinne', '1', '0', 'Human_bc6e409e-24db-11e8-a352-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Finne', '1', '0', 'Human_e4810aa1-24db-11e8-8760-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Gear', '1', '0', 'Human_ed89a9de-24db-11e8-88bf-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Hyacinth', '1', '0', 'Human_fece2280-24db-11e8-b31d-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Iris', '1', '0', 'Human_10cc2721-24dc-11e8-b995-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Jesmine', '1', '0', 'Human_22127480-24dc-11e8-8a9b-2c4d54f01cf2');
INSERT INTO `ren_user` VALUES ('Publisher', '1', '1', 'Human_c8ed5d9e-250a-11e8-9c5e-2c4d54f01cf2');
