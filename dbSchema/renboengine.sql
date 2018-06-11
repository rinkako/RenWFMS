/*

Source Server         : localhost_3306
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : renboengine

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-11 20:06:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ren_archived_tree`
-- ----------------------------
DROP TABLE IF EXISTS `ren_archived_tree`;
CREATE TABLE `ren_archived_tree` (
  `rtid` varchar(64) NOT NULL,
  `tree` longtext NOT NULL,
  PRIMARY KEY (`rtid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_archived_tree
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_authuser`
-- ----------------------------
DROP TABLE IF EXISTS `ren_authuser`;
CREATE TABLE `ren_authuser` (
  `username` varchar(255) NOT NULL,
  `domain` varchar(255) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `password` varchar(255) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `createtimestamp` datetime DEFAULT NULL,
  `lastlogin` datetime DEFAULT NULL,
  `gid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`,`domain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_authuser
-- ----------------------------
INSERT INTO `ren_authuser` VALUES ('admin', 'admin', '999', 'ba806ebb361eb1083aa68cd97557254f9324d381c3d3f4abbc97e7b51f83b6bb', '0', '2018-01-01 00:00:01', '2018-02-22 22:22:22', null);
INSERT INTO `ren_authuser` VALUES ('admin', 'sysu', '1', 'ba806ebb361eb1083aa68cd97557254f9324d381c3d3f4abbc97e7b51f83b6bb', '0', '2018-06-01 16:33:24', null, null);
INSERT INTO `ren_authuser` VALUES ('alice', 'admin', '0', 'b2205cb26f33c4568e8e09eccb263cf3ea5abea177c310487f53f727b4be7561', '0', '2018-06-03 21:56:06', null, 'Human_fb10f3c0-1261-11e8-9bd4-5404a6a99e5d');
INSERT INTO `ren_authuser` VALUES ('test', 'admin', '0', '7fd8c26d47959d69a8b9874cb050e225145c59265ea42edd766562ee163469a9', '0', '2018-06-02 17:21:52', null, '16883');

-- ----------------------------
-- Table structure for `ren_binstep`
-- ----------------------------
DROP TABLE IF EXISTS `ren_binstep`;
CREATE TABLE `ren_binstep` (
  `nodeId` varchar(64) NOT NULL,
  `rtid` varchar(64) NOT NULL,
  `supervisorId` varchar(64) NOT NULL DEFAULT '',
  `notifiableId` text NOT NULL,
  `binlog` longblob,
  PRIMARY KEY (`nodeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_binstep
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_bo`
-- ----------------------------
DROP TABLE IF EXISTS `ren_bo`;
CREATE TABLE `ren_bo` (
  `boid` varchar(64) NOT NULL,
  `bo_name` text NOT NULL,
  `pid` varchar(64) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0',
  `bo_content` text,
  `serialized` blob,
  `broles` text,
  PRIMARY KEY (`boid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_bo
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_domain`
-- ----------------------------
DROP TABLE IF EXISTS `ren_domain`;
CREATE TABLE `ren_domain` (
  `name` varchar(128) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `createtimestamp` datetime DEFAULT NULL,
  `corgan_gateway` text,
  `urlsafe_signature` text,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_domain
-- ----------------------------
INSERT INTO `ren_domain` VALUES ('admin', '2', '0', '2018-02-01 22:57:23', 'http://localhost:10235/api/', 'PrUpNw1dM3zRH6j3eviklCHE9Zbvk9NavGcJ_CibW19h50Yvr-ZZYZqn5Gi_SG1cPVQEIZf2wAJgBmq4dhNj7w7t9wUEz2pcGhn-6kIRO--QqWy121gksPE8B103RtMzuOsQDcErk4LriRQRO7-Xqks-RtpBUnpInnS_lkkajQs');
INSERT INTO `ren_domain` VALUES ('sysu', '0', '0', '2018-06-01 16:33:24', 'http://localhost:10235/sysuapi', 'ejRqaEUStwk3L-UhrMRBM0qPjO860GwRDDbBhFENGv4v0rq3By5vu9DWck9SZxOW6B4EJMdRMvLdm9_DRxgerv5OnwBWeK2LMqi3SBrH79xw4ncq_86xSSvIrSUfSTLxIRBx8NxxwBdP-GI4DTGBojOZZu0atHstx1qP4Qt-rcA');

-- ----------------------------
-- Table structure for `ren_domainpay`
-- ----------------------------
DROP TABLE IF EXISTS `ren_domainpay`;
CREATE TABLE `ren_domainpay` (
  `domain` varchar(128) NOT NULL,
  `paymentType` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`domain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_domainpay
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_exitem`
-- ----------------------------
DROP TABLE IF EXISTS `ren_exitem`;
CREATE TABLE `ren_exitem` (
  `workitemId` varchar(64) NOT NULL,
  `rtid` varchar(64) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `visibility` int(11) NOT NULL DEFAULT '0' COMMENT '0-domain,1-WFMSadmin',
  `handlerAuthName` varchar(255) DEFAULT NULL,
  `timestamp` datetime NOT NULL,
  `reason` text,
  PRIMARY KEY (`workitemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_exitem
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_log`
-- ----------------------------
DROP TABLE IF EXISTS `ren_log`;
CREATE TABLE `ren_log` (
  `logid` varchar(64) NOT NULL,
  `label` varchar(64) DEFAULT NULL,
  `level` varchar(16) DEFAULT NULL,
  `message` text,
  `timestamp` datetime DEFAULT NULL,
  `rtid` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`logid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_log
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_nsconfig`
-- ----------------------------
DROP TABLE IF EXISTS `ren_nsconfig`;
CREATE TABLE `ren_nsconfig` (
  `ckey` varchar(128) NOT NULL,
  `cvalue` text,
  PRIMARY KEY (`ckey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_nsconfig
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_ns_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `ren_ns_transaction`;
CREATE TABLE `ren_ns_transaction` (
  `nsid` varchar(64) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `rtid` varchar(64) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  `context` text,
  `accept_timestamp` datetime DEFAULT NULL,
  `finish_timestamp` datetime DEFAULT NULL,
  `request_invoker` varchar(64) DEFAULT NULL,
  `scheduled_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`nsid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_ns_transaction
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_process`
-- ----------------------------
DROP TABLE IF EXISTS `ren_process`;
CREATE TABLE `ren_process` (
  `pid` varchar(64) NOT NULL,
  `process_name` text NOT NULL,
  `main_bo` varchar(64) NOT NULL,
  `creator_renid` text NOT NULL,
  `create_timestamp` datetime DEFAULT NULL,
  `launch_count` int(11) NOT NULL DEFAULT '0',
  `success_count` int(11) NOT NULL DEFAULT '0',
  `last_launch_timestamp` datetime DEFAULT NULL,
  `average_cost` bigint(20) NOT NULL DEFAULT '0',
  `state` int(11) NOT NULL DEFAULT '0',
  `authtype` int(11) NOT NULL DEFAULT '0',
  `selfsignature` text,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_process
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_queueitems`
-- ----------------------------
DROP TABLE IF EXISTS `ren_queueitems`;
CREATE TABLE `ren_queueitems` (
  `workqueueId` varchar(64) NOT NULL,
  `workitemId` varchar(64) NOT NULL,
  PRIMARY KEY (`workqueueId`,`workitemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_queueitems
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_rolemap`
-- ----------------------------
DROP TABLE IF EXISTS `ren_rolemap`;
CREATE TABLE `ren_rolemap` (
  `map_id` varchar(64) NOT NULL,
  `rtid` varchar(64) NOT NULL,
  `brole_name` text NOT NULL,
  `corgan_gid` varchar(64) NOT NULL,
  `mapped_gid` varchar(64) NOT NULL,
  `data_version` varchar(64) NOT NULL,
  PRIMARY KEY (`map_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_rolemap
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_rolemap_archived`
-- ----------------------------
DROP TABLE IF EXISTS `ren_rolemap_archived`;
CREATE TABLE `ren_rolemap_archived` (
  `map_id` varchar(64) NOT NULL,
  `rtid` varchar(64) NOT NULL,
  `brole_name` text NOT NULL,
  `corgan_gid` varchar(64) NOT NULL,
  `mapped_gid` varchar(64) NOT NULL,
  `data_version` varchar(64) NOT NULL,
  PRIMARY KEY (`map_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_rolemap_archived
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_rseventlog`
-- ----------------------------
DROP TABLE IF EXISTS `ren_rseventlog`;
CREATE TABLE `ren_rseventlog` (
  `rsevid` varchar(64) NOT NULL,
  `wid` varchar(64) NOT NULL,
  `taskid` varchar(64) NOT NULL,
  `processid` varchar(64) NOT NULL,
  `workerid` varchar(64) NOT NULL,
  `event` varchar(32) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`rsevid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_rseventlog
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_rsparticipant`
-- ----------------------------
DROP TABLE IF EXISTS `ren_rsparticipant`;
CREATE TABLE `ren_rsparticipant` (
  `workerid` varchar(64) NOT NULL DEFAULT 'worker global id',
  `displayname` text COMMENT 'user friendly display name',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '0-human,1-agent',
  `reentrantType` int(11) NOT NULL DEFAULT '0' COMMENT 'is agent reentrant, 0-yes, 1-no',
  `referenceCounter` int(11) NOT NULL DEFAULT '0' COMMENT 'reference counter for running processes',
  `agentLocation` text,
  `note` text,
  PRIMARY KEY (`workerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_rsparticipant
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_rsrecord`
-- ----------------------------
DROP TABLE IF EXISTS `ren_rsrecord`;
CREATE TABLE `ren_rsrecord` (
  `rstid` varchar(64) NOT NULL,
  `rtid` varchar(64) NOT NULL,
  `resourcing_id` varchar(64) NOT NULL,
  `receive_timestamp` datetime DEFAULT NULL,
  `scheduled_timestamp` datetime DEFAULT NULL,
  `finish_timestamp` datetime DEFAULT NULL,
  `execution_timespan` bigint(20) DEFAULT NULL,
  `is_succeed` int(11) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  `service` text,
  `args` text,
  PRIMARY KEY (`rstid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_rsrecord
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_rstask`
-- ----------------------------
DROP TABLE IF EXISTS `ren_rstask`;
CREATE TABLE `ren_rstask` (
  `taskid` varchar(64) NOT NULL COMMENT 'global id',
  `boid` varchar(64) NOT NULL COMMENT 'belong to boid',
  `polymorphism_name` text NOT NULL COMMENT 'name for task',
  `polymorphism_id` text NOT NULL COMMENT 'id for task',
  `brole` text NOT NULL,
  `principle` text,
  `eventdescriptor` text NOT NULL COMMENT 'event name like eventSuccess in descriptor json string',
  `hookdescriptor` text NOT NULL COMMENT 'post hooks like onOffer in descriptor json string',
  `documentation` text NOT NULL,
  `parameters` longtext NOT NULL COMMENT 'parameter in JSON',
  PRIMARY KEY (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_rstask
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_runtimerecord`
-- ----------------------------
DROP TABLE IF EXISTS `ren_runtimerecord`;
CREATE TABLE `ren_runtimerecord` (
  `rtid` varchar(64) NOT NULL,
  `process_id` varchar(64) DEFAULT NULL,
  `process_name` text,
  `session_id` varchar(64) DEFAULT NULL,
  `launch_authority_id` varchar(64) DEFAULT NULL,
  `launch_timestamp` datetime DEFAULT NULL,
  `launch_from` text,
  `launch_type` int(11) DEFAULT NULL,
  `tag` text,
  `interpreter_id` varchar(64) DEFAULT NULL,
  `resourcing_id` varchar(64) DEFAULT NULL,
  `resource_binding` text,
  `resource_binding_type` int(11) DEFAULT NULL,
  `failure_type` int(11) DEFAULT NULL,
  `participant_cache` text,
  `finish_timestamp` datetime DEFAULT NULL,
  `is_succeed` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`rtid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_runtimerecord
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_session`
-- ----------------------------
DROP TABLE IF EXISTS `ren_session`;
CREATE TABLE `ren_session` (
  `token` varchar(64) NOT NULL,
  `username` text NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `create_timestamp` datetime NOT NULL,
  `until_timestamp` datetime DEFAULT NULL,
  `destroy_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_session
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_webuilog`
-- ----------------------------
DROP TABLE IF EXISTS `ren_webuilog`;
CREATE TABLE `ren_webuilog` (
  `logid` varchar(64) NOT NULL,
  `label` varchar(64) DEFAULT NULL,
  `level` varchar(16) DEFAULT NULL,
  `message` text,
  `timestamp` datetime DEFAULT NULL,
  `rtid` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`logid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_webuilog
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_workitem`
-- ----------------------------
DROP TABLE IF EXISTS `ren_workitem`;
CREATE TABLE `ren_workitem` (
  `wid` varchar(64) NOT NULL COMMENT 'workitem global id',
  `rtid` varchar(64) NOT NULL COMMENT 'belong to runtime record id',
  `resourcing_id` varchar(64) NOT NULL COMMENT 'resource service instance id',
  `processId` varchar(64) NOT NULL COMMENT 'belong to process global id',
  `boId` varchar(64) NOT NULL COMMENT 'belong to bo global id',
  `taskid` text NOT NULL COMMENT 'task id which is unique in a process',
  `taskPolymorphismId` text NOT NULL COMMENT 'task global id',
  `arguments` longtext COMMENT 'arguments for subsystem',
  `firingTime` datetime DEFAULT NULL,
  `enablementTime` datetime DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `completionTime` datetime DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL COMMENT 'workitem status whether valid or not',
  `resourceStatus` varchar(128) DEFAULT NULL COMMENT 'resourcing status such as unoffer',
  `startedBy` varchar(64) DEFAULT NULL COMMENT 'complete by what resource global id',
  `completedBy` varchar(64) DEFAULT NULL COMMENT 'complete by what resource global id',
  `timertrigger` varchar(64) DEFAULT NULL COMMENT 'trigger timer global id',
  `timerexpiry` varchar(64) DEFAULT NULL COMMENT 'expiry timer global id',
  `latestStartTime` datetime DEFAULT NULL COMMENT 'last start timestamp',
  `executeTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'execution time cost in ms',
  `callbackNodeId` varchar(64) NOT NULL,
  PRIMARY KEY (`wid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_workitem
-- ----------------------------

-- ----------------------------
-- Table structure for `ren_workqueue`
-- ----------------------------
DROP TABLE IF EXISTS `ren_workqueue`;
CREATE TABLE `ren_workqueue` (
  `queueId` varchar(64) NOT NULL,
  `ownerId` varchar(64) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`queueId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ren_workqueue
-- ----------------------------
