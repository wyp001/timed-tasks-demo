/*
Navicat MySQL Data Transfer

Source Server         : LocalMysql
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : db_quartz

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2020-11-18 15:07:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名',
  `description` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  `bean_class` varchar(255) DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
  `job_status` varchar(255) DEFAULT NULL COMMENT '任务状态',
  `job_group` varchar(255) DEFAULT NULL COMMENT '任务分组',
  `create_user` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES ('1', 'helloword', '跑批配置测试', '0/2 * * * * ?', 'com.wyp.quartzdemo.tasks.HelloWorldJob', '1', 'group', 'zhangzhuo', '2018-06-29 23:31:42', 'zhangzhuo', '2018-06-29 23:31:42');
INSERT INTO `sys_task` VALUES ('2', 'job02', '定时任务02', '0/5 * * * * ?', 'com.wyp.quartzdemo.tasks.JobTask02', '1', 'group02', 'aaa', '2020-11-18 14:45:36', 'aaaa', '2020-11-18 14:45:46');
