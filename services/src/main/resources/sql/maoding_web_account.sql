/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 172.16.6.71:3306
Source Database       : maoding_qa

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-01-31 17:43:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_account
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_account`;
CREATE TABLE `maoding_web_account` (
  `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
  `user_name` varchar(30) DEFAULT NULL COMMENT '用户名（冗余）',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '昵称',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `cellphone` varchar(11) NOT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL,
  `default_company_id` varchar(32) DEFAULT NULL COMMENT '默认企业id',
  `signature` varchar(100) DEFAULT NULL COMMENT '个性签名',
  `status` varchar(1) DEFAULT '0' COMMENT '账号状态(1:未激活，0：激活）',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `emial_code` varchar(100) DEFAULT NULL COMMENT '邮箱绑定(验证）格式：邮箱-验证码（email-code）',
  `active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cellphone` (`cellphone`),
  KEY `default_company_id` (`default_company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录用户表';

-- ----------------------------
-- Records of maoding_web_account
-- ----------------------------
INSERT INTO `maoding_web_account` VALUES ('03f58e96152b434e9e7c200cce05a0b4', 'Dorde', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13924600474', '', '', '', '1', '2017-11-07 14:18:02', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('05536f6d423240e3bf61d0b1a9c50b6a', '测试test', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18589035566', '', '', '', '0', '2017-09-16 09:28:07', '', '2017-09-25 16:20:48', '', '', '2017-09-25 16:15:36');
INSERT INTO `maoding_web_account` VALUES ('0623b7a797ac4341aaf2220bb375d670', '张小芬', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13538295829', '', '', '', '1', '2017-11-07 14:22:52', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('0657a5422cec414e9cf36cc86c455815', '王  珊', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13501584626', '', '', '', '1', '2017-11-07 14:22:49', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('0979366978d44ce8ab8faf1c7d80c137', '文朵', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18038026646', '', '', '', '0', '2017-09-19 10:48:06', '', '2017-09-21 15:00:54', '', '', '2017-09-21 14:56:47');
INSERT INTO `maoding_web_account` VALUES ('0b319afc3d964600b70babea437ead3b', '伍娜', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13410177865', '', '', '', '1', '2017-11-07 14:22:57', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('0d0860b5b0b64a919deb0a8626efc6e4', '曹宇欢', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18316622537', '', '', '', '1', '2017-11-07 14:22:55', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('0f2845b122f04e44b0962cd41d840c48', '邓鹏程', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13480109911', '', '', '', '1', '2017-11-07 14:22:54', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('11d7d9c4ea8f4dbf903b9c89bfe63f30', 'dr', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13145555770', '', '', '', '0', '2017-09-21 15:59:16', '', '2017-09-21 15:59:56', '', '', '2017-09-21 15:55:49');
INSERT INTO `maoding_web_account` VALUES ('123f9ef123c140fd9b6c7a5123c68e1a', '卯丁助手', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '', '', '', '0', '2016-12-26 14:51:40', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('1386e41710014304816dd7ffeda3275f', 'saber', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15606767157', '', '', '', '0', '2017-09-18 09:46:15', '', null, '', '', '2017-09-18 09:42:12');
INSERT INTO `maoding_web_account` VALUES ('19ea83f3c3eb4097acbbf43b27f49765', '温盛健', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18620251390', null, null, null, '1', '2018-01-17 19:01:52', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('26797513720a4489a5c940c23e1f0521', '圣剑', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18620251394', '', '', '', '0', '2017-09-28 18:14:39', '', null, '', '', '2017-09-28 18:09:24');
INSERT INTO `maoding_web_account` VALUES ('2ba2e7a5055e4e1f892690f536616359', '张三5', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13012345567', '', '', '', '1', '2017-09-21 18:08:57', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('2e32462b721044c8bb7f53374092609a', '刘文锋', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13670294262', '', '', '', '1', '2017-11-07 14:22:50', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('31ca622c7b364303bbe27f93b07b7180', '杨萍', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15876722666', '', '', '', '1', '2017-11-07 14:17:59', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('328a4fc9dce94b62a1ed6d0a57207b16', '9527', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13647712556', '', '', '', '0', '2017-09-16 11:22:56', '', '2017-09-29 14:23:44', '', '', '2017-09-16 11:18:55');
INSERT INTO `maoding_web_account` VALUES ('328d11d4ab654c04ab4a85f36999a630', '李广华', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13728782265', '', '', '', '1', '2017-11-07 14:22:48', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('34c7c9644a714b2684272c0998d9b552', '王汝彬', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18620154150', null, null, null, '1', '2018-01-17 19:01:52', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('38a3d7c3eda445c289ac8d9cafb60206', '老冒', '', 'FED31FB124D94E121E5F2C372FDDA877', '13602672998', '', '', '', '0', '2017-09-16 11:58:12', '', null, '', '', '2017-09-16 11:54:11');
INSERT INTO `maoding_web_account` VALUES ('39f8c055a7b44647ba7250ecf1d5abd1', '王华斌', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18565707492', '', '', '', '1', '2017-11-07 14:22:56', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('3e2f62cfbff44466900d84755c212000', '沐丫头', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18188616181', null, null, null, '0', '2018-01-29 15:06:43', null, '2018-01-29 15:07:30', null, null, '2018-01-29 14:56:29');
INSERT INTO `maoding_web_account` VALUES ('3e9941e0059049b58166b77365db2b20', '张  骐', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13923426730', '', '', '', '1', '2017-11-07 14:17:58', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('41236607196e4db3aca0849c70c8fa42', '卢忻', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13926516981', '', '', '', '1', '2017-11-28 11:36:50', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('41d244733ec54f09a255836637f2b21d', '张成亮', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13680809727', '', '', '', '0', '2017-09-18 10:09:06', '', null, '', '', '2017-09-18 10:05:03');
INSERT INTO `maoding_web_account` VALUES ('4736bc67a64f418e9a585f303e95eb2b', '许佳迪', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15353032981', '', '', '', '0', '2017-09-18 10:19:36', '', null, '', '', '2017-09-18 10:15:33');
INSERT INTO `maoding_web_account` VALUES ('48040b528acd474388669c26f34c64b3', '王迪钦', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13006654553', '', '', '', '1', '2017-11-07 14:22:46', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('496978b03f8041c3ae9449a699e37643', '小卯', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15119577622', '', '', '', '0', '2017-09-16 11:47:37', '', '2017-09-30 14:28:14', '', '', '2017-09-22 10:40:30');
INSERT INTO `maoding_web_account` VALUES ('4e658e6c7be0454cbb4c694977b2fd04', 'wells', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18871465314', '', '', '', '0', '2017-09-16 10:30:29', '', '2017-09-21 15:19:21', '', '', '2017-09-16 10:26:28');
INSERT INTO `maoding_web_account` VALUES ('4f85619b3df3478085f505423a8ce5ef', '吴', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13631254795', '', '', '', '1', '2017-09-26 14:30:23', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('5201a47a25eb40dc883bb3cb2111272e', '康哥', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15527140009', '', '', '', '0', '2017-09-30 10:51:55', '', null, '', '', '2017-09-30 10:51:58');
INSERT INTO `maoding_web_account` VALUES ('53eab2b9ab2b4a3986cdc0ef79ab8663', '刘昌萍', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13530050560', '', '', '', '1', '2017-11-07 14:22:47', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('54c458486e3b4b6fbf4d9a65b40d3edd', 'wrb', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18620154157', '', '', '', '0', '2017-09-19 18:23:16', '', null, '', '', '2017-09-19 18:19:11');
INSERT INTO `maoding_web_account` VALUES ('5728c548d2d14cf28957a0ef839d0569', 'lqyQA', '', '21218CCA77804D2BA1922C33E0151105', '15712008191', '', '', '', '0', '2017-10-10 18:29:33', '', null, '', '', '2017-10-10 18:22:03');
INSERT INTO `maoding_web_account` VALUES ('587263321f164d30b04e3d87ddee90c1', 'Milo', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18681520786', '', '', '', '1', '2017-11-07 14:22:53', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('59960040ef9c4edbb761d5d17c7bfaa5', '王进', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13723470581', null, null, null, '0', '2017-12-26 15:55:42', null, '2017-12-26 15:57:12', null, null, '2017-12-26 15:50:53');
INSERT INTO `maoding_web_account` VALUES ('5bf3883ca3814311ba05ba720be71e9e', '芦文清', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18098958090', '', '', '', '1', '2017-11-07 14:17:58', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('5fea83a913c045eea557f442479b1a9d', '黄永胜', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13480789157', '', '', '', '1', '2017-11-07 14:22:57', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('60f459efa9b3435b9e3e87a2151199a1', '王大勇', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13823153335', '', '', '', '1', '2017-11-07 14:22:47', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('6193790bd7724ccab58a712eecf5db77', 'test人员', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18589035088', '', '', '', '0', '2017-09-18 18:53:38', '', '2017-09-18 18:54:09', '', '', '2017-09-18 18:50:06');
INSERT INTO `maoding_web_account` VALUES ('65ec8fca71e6412db49f3bc5c8e8f3e9', '黄铎', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13510106915', '', '', '', '0', '2017-09-19 14:52:47', '', null, '', '', '2017-09-19 14:48:43');
INSERT INTO `maoding_web_account` VALUES ('65ed6c7747f64badb89f650ebba89954', '张锋', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13823366305', '', '', '', '1', '2017-11-07 14:22:49', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('6649e3c731ba4b44861876473a207e39', '练思云', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15606767150', null, null, null, '1', '2018-01-17 19:01:51', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('6740edac60e24f54b95a7bee90f5e5a1', '刘东', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18589035080', null, null, null, '1', '2018-01-17 19:01:53', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('6f1ccdd0c8f94301a93cd5367687a607', '罗娉婷', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13798291620', null, null, null, '1', '2018-01-17 19:01:50', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('6fd3f2a468a445778ef11e00e86d7960', '小鸡过河', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15820419841', null, null, null, '0', '2018-01-04 15:37:22', null, '2018-01-15 10:52:08', null, null, '2018-01-04 15:29:53');
INSERT INTO `maoding_web_account` VALUES ('729db2a6a81542b298535feb71d35f26', 'tyhty', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15857864852', '', '', '', '1', '2017-09-16 11:26:30', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('7b323228d28444cf8e04b4cb598dddb5', '乙', null, 'E10ADC3949BA59ABBE56E057F20F883E', '91120150502', null, null, null, '0', '2018-01-15 11:14:49', null, null, null, null, '2018-01-15 11:14:49');
INSERT INTO `maoding_web_account` VALUES ('81acab1b20fd4d8d9100d7f9ae9d9f6b', '张某某', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13012345678', '', '', '', '1', '2017-09-21 18:07:19', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('86883812d6db449d99d7c2a969516ba7', '黄  旭', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13723709007', '', '', '', '1', '2017-11-07 14:22:49', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('88cab3a65c6e46429941a7061cf5accc', '陈景冲', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15815508010', '', '', '', '1', '2017-11-07 14:22:53', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('88e6a7981bda44399f544a2dbaeac6a9', '庞琼玉', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18319569361', '', '', '', '1', '2017-11-07 14:18:01', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('8cafb52cdded48158ac3548d76817b49', '戊', null, 'E10ADC3949BA59ABBE56E057F20F883E', '91120150505', null, null, null, '0', '2018-01-15 11:14:50', null, null, null, null, '2018-01-15 11:14:50');
INSERT INTO `maoding_web_account` VALUES ('8dcfab4944ed480086b79f12f2037cf2', '曹金玲', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13632353720', null, null, null, '1', '2018-01-17 19:01:51', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('8f830707afd649328da100311bf90d78', '于源', '', '354474D869F353A7F97456C8EDEFE50B', '13823717716', '', '', '', '0', '2017-11-07 14:22:48', '', '2017-11-08 16:47:08', '', '', '2017-11-08 16:36:02');
INSERT INTO `maoding_web_account` VALUES ('90a5ab936b744ac59e15ca39822ae14b', '甲', null, 'E10ADC3949BA59ABBE56E057F20F883E', '91120150501', null, null, null, '0', '2018-01-15 11:14:49', null, null, null, null, '2018-01-15 11:14:49');
INSERT INTO `maoding_web_account` VALUES ('91e5d8f0daae4b169b7f416fb90dfc47', '庞则庆', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13590350993', '', '', '', '1', '2017-11-07 14:18:00', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('93d0e47be3c045b5aa36123553a0d82e', '古  强', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13691969508', '', '', '', '1', '2017-11-07 14:22:50', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('94e8b930450c428685e2258e189449e2', '陶均明', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15112308656', '', '', '', '0', '2017-09-18 15:35:27', '', '2017-09-18 15:35:57', '', '', '2017-09-18 15:31:54');
INSERT INTO `maoding_web_account` VALUES ('9672831a574b4c4faf800a17295c8193', '沐小胖', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13798291624', '', '', '', '0', '2017-09-18 17:12:50', '', null, '', '', '2017-09-18 17:08:47');
INSERT INTO `maoding_web_account` VALUES ('97e5e25c3c6844a1b12194491432d013', '罗沐沐', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18188616180', null, null, null, '0', '2018-01-29 14:02:41', null, '2018-01-29 14:07:43', null, null, '2018-01-29 13:56:43');
INSERT INTO `maoding_web_account` VALUES ('98c5f45a30ce4680ab38ab09d917bf5f', '张成亮', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13680809720', null, null, null, '1', '2018-01-17 19:01:52', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('9bb0d2ccbe164df2a3056e7f38be0a84', 'jack', '', 'E10ADC3949BA59ABBE56E057F20F883E', '17688909517', '', '', '', '0', '2017-09-16 10:48:48', '', '2017-09-16 10:49:04', '', '', '2017-09-16 10:45:03');
INSERT INTO `maoding_web_account` VALUES ('9e7b9201f2c149cdbd365b0bcb3d0587', '张三', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13012345568', '', '', '', '1', '2017-09-21 18:08:57', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('9f74cad9622d42adafdc9ecfc7ff1e8d', '周昱如', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18617049077', '', '', '', '1', '2017-11-07 14:22:56', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('a3efed4641614d79a2a1ec16b6f8a5f4', 'hhuh', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13954862569', '', '', '', '1', '2017-09-16 11:26:54', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('a6084d0c2e0a4ade8c10aa7fac3ddc4a', '格老', '', 'FED31FB124D94E121E5F2C372FDDA877', '18098958096', '', '', '', '0', '2017-09-16 11:59:14', '', '2018-01-26 12:04:50', '', '', '2017-09-16 11:55:13');
INSERT INTO `maoding_web_account` VALUES ('aadb9743ef714eab94f8c189fa2bd13a', 'guozhibin', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15013551861', '', '', '', '0', '2017-10-11 09:58:19', '', null, '', '', '2017-10-11 09:50:48');
INSERT INTO `maoding_web_account` VALUES ('ab708980752941219664fc0222cf579a', '郑剑锋', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13823106473', '', '', '', '1', '2017-11-07 14:22:56', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('ab866a6f5071465eba2bea4d34343b64', '马丽芳', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13410841919', '', '', '', '1', '2017-11-07 14:18:02', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('af8a8a82e39e45609d3b7e3879bb31c7', 'li', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15050606060', '', '', '', '1', '2017-10-19 11:45:52', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('b36d9153c62b47b69169ff6c5220cc01', '毛双凤', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18665965060', null, null, null, '1', '2018-01-17 19:01:53', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('b3afa251fa374ce887142b5559492330', '李  凌', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13922888108', '', '', '', '1', '2017-11-07 14:17:58', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('b63c48ce120a4fa680068dd0bf771549', '戌', null, 'E10ADC3949BA59ABBE56E057F20F883E', '91120150506', null, null, null, '0', '2018-01-15 11:14:50', null, null, null, null, '2018-01-15 11:14:50');
INSERT INTO `maoding_web_account` VALUES ('ba719f08555548039caab4fecf131ed7', '刘沁怡', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15712008190', null, null, null, '1', '2018-01-17 19:01:53', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('bb6e5dafa53f4863b8af11904757469d', '丙', null, 'E10ADC3949BA59ABBE56E057F20F883E', '91120150503', null, null, null, '0', '2018-01-15 11:14:49', null, null, null, null, '2018-01-15 11:14:49');
INSERT INTO `maoding_web_account` VALUES ('bb6fe362e501486897af004c5ce8de8d', '丁', null, 'E10ADC3949BA59ABBE56E057F20F883E', '91120150504', null, null, null, '0', '2018-01-15 11:14:50', null, null, null, null, '2018-01-15 11:14:50');
INSERT INTO `maoding_web_account` VALUES ('c0f25c23d8764bb2903fdf969da4371f', '许佳迪', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15353032980', null, null, null, '1', '2018-01-17 19:01:50', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('c33204ed20164cf392f8fd9320fb0744', '丁喜彤', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15818663612', '', '', '', '1', '2017-11-07 14:18:01', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('c41868c87afc4672a90fd044147d20d6', '卢沂', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13926516980', null, null, null, '0', '2018-01-17 19:01:48', null, '2018-01-18 16:41:43', null, null, '2018-01-18 16:41:43');
INSERT INTO `maoding_web_account` VALUES ('c462b268864c4c9495aa56a092c54038', '123', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13221450717', '', '', '', '0', '2017-09-20 18:46:51', '', '2017-09-20 18:47:37', '', '', '2017-09-20 18:43:32');
INSERT INTO `maoding_web_account` VALUES ('c677d62a8ed841929dc9928c87c090d2', '贾  筠', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13924669522', '', '', '', '1', '2017-11-07 14:22:47', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('c73df46576c34cb5a9b037c823cc89ee', '丁亚美', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15118455792', '', '', '', '1', '2017-09-19 15:12:14', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('ca5dab42e0864e9280791dfe9c0054c4', '小卯', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18188616182', '', '', '', '0', '2017-10-09 15:05:25', '', '2018-01-29 13:21:36', '', '', '2018-01-29 12:51:42');
INSERT INTO `maoding_web_account` VALUES ('cb21ad75058a4102827310c59c2616e3', '李世霞', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13510066970', '', '', '', '1', '2017-11-07 14:22:53', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('cb740b4cd62c4fdf8cbe28a032164cc5', '王永峰', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13686800019', '', '', '', '1', '2017-11-07 14:22:49', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('d066bbf9b78b44f7865441c15fdbdcba', '李英', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13620949309', '', '', '', '1', '2017-11-07 14:22:52', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('d3b3cedc158742c783ab31539d965b5f', '杨深', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13714847045', '', '', '', '1', '2017-11-07 14:18:01', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('d5a1b2c3e2a648e98e47782cb5f81a44', '许  峻', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13510213251', '', '', '', '1', '2017-11-07 14:22:48', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('d97bf0198ebd42bc8f695e2940bb9d10', 'xiaofan', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13652381815', '', '', '', '0', '2017-09-18 17:17:27', '', null, '', '', '2017-09-18 17:13:24');
INSERT INTO `maoding_web_account` VALUES ('de4e5b98e7524ae2ad92ffd791658972', '毛双凤', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18665965065', '', '', '', '0', '2017-09-18 15:30:27', '', null, '', '', '2017-09-18 15:26:24');
INSERT INTO `maoding_web_account` VALUES ('deec1fa9f27b4d76860c8f9470a04568', '李长明', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13509672920', '', '', '', '1', '2017-11-07 14:17:57', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('df943cf637314eaabaf0cc62ba209b65', '魏昕', '', '8B7AD7BEAFA86F2BAC2EDD31666E1DB4', '18688715679', '', '', '', '0', '2017-11-07 14:18:00', '', '2018-01-26 10:53:23', '', '', '2018-01-26 10:42:26');
INSERT INTO `maoding_web_account` VALUES ('e17ee8db69014e72ba100c84f15538ae', '周媛', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18929392018', '', '', '', '1', '2017-11-07 14:17:59', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('e34d60e5809541d99638567505779b1f', '王睿', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13723494517', '', '', '', '1', '2017-11-07 14:18:02', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('e357ca5b1bf84bb8b8069fed31b6b5e4', '李成慧', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13823193370', '', '', '', '1', '2017-11-07 14:22:50', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('e3ffcf6df9364ba8bd495c70261ee6c7', 'liudong', '', 'D36BE78469DB5CB868B4C769109A8244', '18589035085', '', '', '', '0', '2017-09-16 09:26:21', '', null, '', '', '2017-09-16 09:22:20');
INSERT INTO `maoding_web_account` VALUES ('e4294664b85141d49f17ef0192d1cce2', '黄铎', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13510106910', null, null, null, '1', '2018-01-17 19:01:49', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('e4adb5ccbedd4d929607a87f342c11a1', '孙勇', '', 'FED31FB124D94E121E5F2C372FDDA877', '13602630000', '', '', '', '0', '2017-09-16 11:59:59', '', '2017-09-16 12:15:07', '', '', '2017-09-16 12:11:06');
INSERT INTO `maoding_web_account` VALUES ('e872354a98dc452980f84d8c704d45a0', '陈旭生', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15816851148', '', '', '', '1', '2017-11-07 14:22:48', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('e8b26b0503c74275a9f54f038e5a481f', '李向北', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18623400666', '', '', '', '1', '2017-11-07 14:22:46', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('eea4700514ba407894e2302a2a8c0366', '曹大大', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13632353729', '', '08e117e5b55a459a84f398a7e2cf4f74', '', '0', '2017-09-16 09:55:47', '', '2018-01-22 19:12:54', '', '', '2018-01-22 19:03:02');
INSERT INTO `maoding_web_account` VALUES ('eef993daaee34854bd346986533e3656', '郭志彬', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15013551860', null, null, null, '1', '2018-01-17 19:01:51', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('f0466447e8754ed99656bfc046138b1b', '李超红', '', 'E576264C356B1A9DB3A960A81F3B9CED', '13603018023', '', '', '', '0', '2017-11-07 14:18:00', '', '2017-11-07 14:22:33', '', '', '2017-11-07 14:11:29');
INSERT INTO `maoding_web_account` VALUES ('f28e340a416c487ab26fa71aa12c8c9a', '张', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13556971235', '', '', '', '1', '2017-09-26 14:30:55', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('f44c9a3a1be941b093567733378e44b0', '李俊', '', 'E10ADC3949BA59ABBE56E057F20F883E', '15220255852', '', '', '', '1', '2017-11-07 14:22:52', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('f54d74f37773418682a7178980148692', '李  和', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13603099451', '', '', '', '1', '2017-11-07 14:22:50', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('f721d27455c6493fae6ac1e0dec4e16e', 'w001', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13631285064', '', '', '', '0', '2017-10-16 14:56:46', '', '2018-01-18 17:14:42', '', '', '2018-01-18 17:04:54');
INSERT INTO `maoding_web_account` VALUES ('f803f06e90004dd79eb81961c217e8ed', '刘  扬', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13902957370', '', '', '', '1', '2017-11-07 14:22:47', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('f895ec747a714cf7a59f1e0d917e1676', '谭金雄', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13823101552', '', '', '', '1', '2017-11-07 14:22:54', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('fe6a017f45d24757b3b1af1abdb3d5fc', '孙  冰', '', 'E10ADC3949BA59ABBE56E057F20F883E', '13600180660', '', '', '', '1', '2017-11-07 14:22:46', '', null, '', '', null);
INSERT INTO `maoding_web_account` VALUES ('fedb500d60cf4053bcd800cba29e06b7', '梁沛江', '', 'E10ADC3949BA59ABBE56E057F20F883E', '18665996791', '', '', '', '1', '2017-11-07 14:22:48', '', null, '', '', null);
