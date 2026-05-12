-- MySQL dump 10.13  Distrib 5.7.31, for Linux (x86_64)
--
-- Host: localhost    Database: jinengjiaoyi
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `jinengjiaoyi`
--

/*!40000 DROP DATABASE IF EXISTS `jinengjiaoyi`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `jinengjiaoyi` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `jinengjiaoyi`;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `adminid` bigint(20) DEFAULT NULL COMMENT '管理员id',
  `ask` longtext COLLATE utf8mb4_unicode_ci COMMENT '提问',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复',
  `isreply` int(11) DEFAULT NULL COMMENT '是否回复',
  `isread` int(11) DEFAULT '0' COMMENT '已读/未读(1:已读,0:未读)',
  `uname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `uimage` longtext COLLATE utf8mb4_unicode_ci COMMENT '用户头像',
  `type` int(11) DEFAULT '1' COMMENT '内容类型(1:文本,2:图片,3:视频,4:文件,5:表情)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI智能问答';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (1,'2026-04-12 05:55:29',1,1,'提问1','回复1',1,1,'用户名1','upload/chat_uimage1.jpg,upload/chat_uimage2.jpg,upload/chat_uimage3.jpg',1),(2,'2026-04-12 05:55:29',2,2,'提问2','回复2',2,2,'用户名2','upload/chat_uimage2.jpg,upload/chat_uimage3.jpg,upload/chat_uimage4.jpg',2),(3,'2026-04-12 05:55:29',3,3,'提问3','回复3',3,3,'用户名3','upload/chat_uimage3.jpg,upload/chat_uimage4.jpg,upload/chat_uimage5.jpg',3),(4,'2026-04-12 05:55:29',4,4,'提问4','回复4',4,4,'用户名4','upload/chat_uimage4.jpg,upload/chat_uimage5.jpg,upload/chat_uimage6.jpg',4),(5,'2026-04-12 05:55:29',5,5,'提问5','回复5',5,5,'用户名5','upload/chat_uimage5.jpg,upload/chat_uimage6.jpg,upload/chat_uimage7.jpg',5),(6,'2026-04-12 05:55:29',6,6,'提问6','回复6',6,6,'用户名6','upload/chat_uimage6.jpg,upload/chat_uimage7.jpg,upload/chat_uimage8.jpg',6),(7,'2026-04-12 05:55:29',7,7,'提问7','回复7',7,7,'用户名7','upload/chat_uimage7.jpg,upload/chat_uimage8.jpg,upload/chat_uimage1.jpg',7),(8,'2026-04-12 05:55:29',8,8,'提问8','回复8',8,8,'用户名8','upload/chat_uimage8.jpg,upload/chat_uimage1.jpg,upload/chat_uimage2.jpg',8);
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chathelper`
--

DROP TABLE IF EXISTS `chathelper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chathelper` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ask` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提问',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天助手表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chathelper`
--

LOCK TABLES `chathelper` WRITE;
/*!40000 ALTER TABLE `chathelper` DISABLE KEYS */;
INSERT INTO `chathelper` VALUES (1,'2026-04-12 05:55:29','提问1','回复1'),(2,'2026-04-12 05:55:29','提问2','回复2'),(3,'2026-04-12 05:55:29','提问3','回复3'),(4,'2026-04-12 05:55:29','提问4','回复4'),(5,'2026-04-12 05:55:29','提问5','回复5'),(6,'2026-04-12 05:55:29','提问6','回复6'),(7,'2026-04-12 05:55:29','提问7','回复7'),(8,'2026-04-12 05:55:29','提问8','回复8');
/*!40000 ALTER TABLE `chathelper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chatmessage`
--

DROP TABLE IF EXISTS `chatmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chatmessage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `fid` bigint(20) NOT NULL COMMENT '好友用户ID',
  `content` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `format` int(11) DEFAULT NULL COMMENT '格式(1:文字，2:图片)',
  `isread` int(11) DEFAULT '0' COMMENT '消息已读(0:未读，1:已读)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chatmessage`
--

LOCK TABLES `chatmessage` WRITE;
/*!40000 ALTER TABLE `chatmessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `chatmessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '配置参数名称',
  `value` varchar(200) DEFAULT NULL COMMENT '配置参数值',
  `url` varchar(500) DEFAULT NULL COMMENT 'url',
  `type` int(11) DEFAULT NULL COMMENT '参数类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='配置文件';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (1,'picture1','upload/picture1.jpg',NULL,1),(2,'picture2','upload/picture2.jpg',NULL,1),(3,'picture3','upload/picture3.jpg',NULL,1),(11,'baidu','{\"appId\":\"49214550\",\"apiKey\":\"7Otjpv2kn0ljQk45qXOXh5MO\",\"secretKey\":\"BMfbXRbTIVaB4C3SbRTtGqDv1wHDvyXS\"}',NULL,2),(12,'qianfan','{\"key\":\"bce-v3/ALTAK-uMJebVb2vAH8S14icvESU/3959ca333edcf60fa175258dc174c613b0288265\"}',NULL,2),(14,'deepseek','{\"key\":\"sk-961af215e60e4f269c2808b3a55eb9e8\"}',NULL,2),(21,'bLoginBackgroundImg','',NULL,3),(22,'bRegisterBackgroundImg','',NULL,3),(23,'bIndexBackgroundImg','',NULL,3),(24,'bTopLogo','',NULL,3),(25,'bHomeLogo','',NULL,3),(26,'fLoginBackgroundImg','',NULL,3),(27,'fRegisterBackgroudImg','',NULL,3),(28,'fTopLogo','',NULL,3);
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discussjinengxuqiu`
--

DROP TABLE IF EXISTS `discussjinengxuqiu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discussjinengxuqiu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `score` double DEFAULT NULL COMMENT '评分',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `thumbsupnum` int(11) DEFAULT '0' COMMENT '赞',
  `crazilynum` int(11) DEFAULT '0' COMMENT '踩',
  `istop` int(11) DEFAULT '0' COMMENT '置顶(1:置顶,0:非置顶)',
  `tuserids` longtext COLLATE utf8mb4_unicode_ci COMMENT '赞用户ids',
  `cuserids` longtext COLLATE utf8mb4_unicode_ci COMMENT '踩用户ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能需求评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discussjinengxuqiu`
--

LOCK TABLES `discussjinengxuqiu` WRITE;
/*!40000 ALTER TABLE `discussjinengxuqiu` DISABLE KEYS */;
/*!40000 ALTER TABLE `discussjinengxuqiu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discussxuexiziliao`
--

DROP TABLE IF EXISTS `discussxuexiziliao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discussxuexiziliao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `thumbsupnum` int(11) DEFAULT '0' COMMENT '赞',
  `crazilynum` int(11) DEFAULT '0' COMMENT '踩',
  `istop` int(11) DEFAULT '0' COMMENT '置顶(1:置顶,0:非置顶)',
  `tuserids` longtext COLLATE utf8mb4_unicode_ci COMMENT '赞用户ids',
  `cuserids` longtext COLLATE utf8mb4_unicode_ci COMMENT '踩用户ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习资料评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discussxuexiziliao`
--

LOCK TABLES `discussxuexiziliao` WRITE;
/*!40000 ALTER TABLE `discussxuexiziliao` DISABLE KEYS */;
/*!40000 ALTER TABLE `discussxuexiziliao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum`
--

DROP TABLE IF EXISTS `forum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `typename` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类名称',
  `title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '帖子标题',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子内容',
  `parentid` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `username` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `isdone` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态',
  `istop` int(11) DEFAULT '0' COMMENT '是否置顶',
  `toptime` datetime DEFAULT NULL COMMENT '置顶时间',
  `cover` longtext COLLATE utf8mb4_unicode_ci COMMENT '封面',
  `isanon` int(11) DEFAULT '0' COMMENT '是否匿名(1:是,0:否)',
  `delflag` int(11) DEFAULT '0' COMMENT '是否删除(1:是,0:否)',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '审核回复',
  `userid` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  KEY `typename` (`typename`),
  CONSTRAINT `forum_ibfk_1` FOREIGN KEY (`typename`) REFERENCES `forumtype` (`typename`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能社区';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum`
--

LOCK TABLES `forum` WRITE;
/*!40000 ALTER TABLE `forum` DISABLE KEYS */;
INSERT INTO `forum` VALUES (1,'2026-04-12 05:55:29','学习','文言文翻译','文言文段落翻译、字词注释、文章赏析、解题指导',0,'用户名1','upload/forum_avatarurl1.jpg,upload/forum_avatarurl2.jpg,upload/forum_avatarurl3.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover1.jpg,upload/forum_cover2.jpg,upload/forum_cover3.jpg',1,0,'是','',1),(2,'2026-04-12 05:55:29','生活','摄影技巧教学','摄影技巧教学、校园约拍指导、照片后期调色、器材选择建议',0,'用户名2','upload/forum_avatarurl2.jpg,upload/forum_avatarurl3.jpg,upload/forum_avatarurl4.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover2.jpg,upload/forum_cover3.jpg,upload/forum_cover4.jpg',2,0,'是','',2),(3,'2026-04-12 05:55:29','生活','配音技巧教学','配音技巧教学、声音美化训练、角色配音指导、音频录制',0,'用户名3','upload/forum_avatarurl3.jpg,upload/forum_avatarurl4.jpg,upload/forum_avatarurl5.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover3.jpg,upload/forum_cover4.jpg,upload/forum_cover5.jpg',3,0,'是','',3),(4,'2026-04-12 05:55:29','生活','手工DIY教学','折纸技巧教学、黏土造型制作、编织饰品教学、手工材料推荐',0,'用户名4','upload/forum_avatarurl4.jpg,upload/forum_avatarurl5.jpg,upload/forum_avatarurl6.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover4.jpg,upload/forum_cover5.jpg,upload/forum_cover6.jpg',4,0,'是','',4),(5,'2026-04-12 05:55:29','工具','短视频剪辑','短视频剪辑、字幕添加、特效包装、配乐选择',0,'用户名5','upload/forum_avatarurl5.jpg,upload/forum_avatarurl6.jpg,upload/forum_avatarurl7.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover5.jpg,upload/forum_cover6.jpg,upload/forum_cover7.jpg',5,0,'是','',5),(6,'2026-04-12 05:55:29','学习','硬笔书法','硬笔书法入门教学、书写姿势矫正、作品点评修改',0,'用户名6','upload/forum_avatarurl6.jpg,upload/forum_avatarurl7.jpg,upload/forum_avatarurl8.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover6.jpg,upload/forum_cover7.jpg,upload/forum_cover8.jpg',6,0,'是','',6),(7,'2026-04-12 05:55:29','学习','PPT美化设计','PPT模板定制、排版优化、动画设计、内容提炼',0,'用户名7','upload/forum_avatarurl7.jpg,upload/forum_avatarurl8.jpg,upload/forum_avatarurl1.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover7.jpg,upload/forum_cover8.jpg,upload/forum_cover1.jpg',7,0,'是','',7),(8,'2026-04-12 05:55:29','艺术','吉他弹唱教学','吉他和弦教学、节奏训练、弹唱技巧指导、曲目练习',0,'用户名8','upload/forum_avatarurl8.jpg,upload/forum_avatarurl1.jpg,upload/forum_avatarurl2.jpg','开放',0,'2026-04-12 13:55:29','upload/forum_cover8.jpg,upload/forum_cover1.jpg,upload/forum_cover2.jpg',8,0,'是','',8);
/*!40000 ALTER TABLE `forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forumtype`
--

DROP TABLE IF EXISTS `forumtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forumtype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `typename` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`),
  KEY `forumtype_ba7j` (`typename`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='论坛类型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forumtype`
--

LOCK TABLES `forumtype` WRITE;
/*!40000 ALTER TABLE `forumtype` DISABLE KEYS */;
INSERT INTO `forumtype` VALUES (1,'2026-04-12 05:55:29','学习'),(2,'2026-04-12 05:55:29','生活'),(3,'2026-04-12 05:55:29','生活'),(4,'2026-04-12 05:55:29','生活'),(5,'2026-04-12 05:55:29','工具'),(6,'2026-04-12 05:55:29','学习'),(7,'2026-04-12 05:55:29','学习'),(8,'2026-04-12 05:55:29','艺术');
/*!40000 ALTER TABLE `forumtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `fid` bigint(20) NOT NULL COMMENT '好友用户ID',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `picture` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `role` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表名',
  `alias` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '别名',
  `type` int(11) DEFAULT '0' COMMENT '类型(0:好友申请，1:好友，2:消息)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `huiyuanchongzhi`
--

DROP TABLE IF EXISTS `huiyuanchongzhi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `huiyuanchongzhi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `chongzhibianhao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '充值编号',
  `tupian` longtext COLLATE utf8mb4_unicode_ci COMMENT '图片',
  `chongzhijine` int(11) DEFAULT NULL COMMENT '充值金额',
  `chongzhiriqi` date DEFAULT NULL COMMENT '充值日期',
  `yonghuzhanghao` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `chongzhibianhao` (`chongzhibianhao`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员充值';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `huiyuanchongzhi`
--

LOCK TABLES `huiyuanchongzhi` WRITE;
/*!40000 ALTER TABLE `huiyuanchongzhi` DISABLE KEYS */;
INSERT INTO `huiyuanchongzhi` VALUES (1,'2026-04-12 05:55:28','1111111111','upload/huiyuanchongzhi_tupian1.jpg,upload/huiyuanchongzhi_tupian2.jpg,upload/huiyuanchongzhi_tupian3.jpg',1,'2026-04-12','15','赵阳'),(2,'2026-04-12 05:55:28','2222222222','upload/huiyuanchongzhi_tupian2.jpg,upload/huiyuanchongzhi_tupian3.jpg,upload/huiyuanchongzhi_tupian4.jpg',2,'2026-04-12','18','吴芳'),(3,'2026-04-12 05:55:28','3333333333','upload/huiyuanchongzhi_tupian3.jpg,upload/huiyuanchongzhi_tupian4.jpg,upload/huiyuanchongzhi_tupian5.jpg',3,'2026-04-12','14','陈曦'),(4,'2026-04-12 05:55:28','4444444444','upload/huiyuanchongzhi_tupian4.jpg,upload/huiyuanchongzhi_tupian5.jpg,upload/huiyuanchongzhi_tupian6.jpg',4,'2026-04-12','12','李娜'),(5,'2026-04-12 05:55:28','5555555555','upload/huiyuanchongzhi_tupian5.jpg,upload/huiyuanchongzhi_tupian6.jpg,upload/huiyuanchongzhi_tupian7.jpg',5,'2026-04-12','11','张强'),(6,'2026-04-12 05:55:28','6666666666','upload/huiyuanchongzhi_tupian6.jpg,upload/huiyuanchongzhi_tupian7.jpg,upload/huiyuanchongzhi_tupian8.jpg',6,'2026-04-12','17','周凯'),(7,'2026-04-12 05:55:28','7777777777','upload/huiyuanchongzhi_tupian7.jpg,upload/huiyuanchongzhi_tupian8.jpg,upload/huiyuanchongzhi_tupian1.jpg',7,'2026-04-12','16','刘敏'),(8,'2026-04-12 05:55:28','8888888888','upload/huiyuanchongzhi_tupian8.jpg,upload/huiyuanchongzhi_tupian1.jpg,upload/huiyuanchongzhi_tupian2.jpg',8,'2026-04-12','13','王浩');
/*!40000 ALTER TABLE `huiyuanchongzhi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiaohuanjilu`
--

DROP TABLE IF EXISTS `jiaohuanjilu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiaohuanjilu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuzhanghao` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `shoujihao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `biaoti` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `jinengfenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '技能分类',
  `gongjitupian` longtext COLLATE utf8mb4_unicode_ci COMMENT '供给图片',
  `jiaohuanjineng` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交换技能',
  `gongjishipin` longtext COLLATE utf8mb4_unicode_ci COMMENT '供给视频',
  `gongjifujian` longtext COLLATE utf8mb4_unicode_ci COMMENT '供给附件',
  `jiaohuanshijian` date DEFAULT NULL COMMENT '交换时间',
  `gongjineirong` longtext COLLATE utf8mb4_unicode_ci COMMENT '供给内容',
  `gongjirenzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '供给人账号',
  `gongjirenxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '供给人姓名',
  `lianxifangshi` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `crossuserid` bigint(20) DEFAULT NULL COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT NULL COMMENT '跨表主键id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交换记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiaohuanjilu`
--

LOCK TABLES `jiaohuanjilu` WRITE;
/*!40000 ALTER TABLE `jiaohuanjilu` DISABLE KEYS */;
INSERT INTO `jiaohuanjilu` VALUES (1,'2026-04-12 05:55:28','15','赵阳','13012345678','文言文翻译','学习','upload/jiaohuanjilu_文言文翻译1.jpg,upload/jiaohuanjilu_文言文翻译2.jpg,upload/jiaohuanjilu_文言文翻译3.jpg','学习','','','2026-04-12','供给内容1','15','赵阳','13012345678',1,1),(2,'2026-04-12 05:55:28','18','吴芳','15065432109','摄影技巧教学','生活','upload/jiaohuanjilu_摄影技巧教学1.jpg,upload/jiaohuanjilu_摄影技巧教学2.jpg,upload/jiaohuanjilu_摄影技巧教学3.jpg','生活','','','2026-04-12','供给内容2','18','吴芳','15065432109',2,2),(3,'2026-04-12 05:55:28','14','陈曦','13890123456','配音技巧教学','生活','upload/jiaohuanjilu_配音技巧教学1.jpg,upload/jiaohuanjilu_配音技巧教学2.jpg,upload/jiaohuanjilu_配音技巧教学3.jpg','生活','','','2026-04-12','供给内容3','14','陈曦','13890123456',3,3),(4,'2026-04-12 05:55:28','12','李娜','15187654321','手工DIY教学','生活','upload/jiaohuanjilu_手工DIY教学1.jpg,upload/jiaohuanjilu_手工DIY教学2.jpg,upload/jiaohuanjilu_手工DIY教学3.jpg','生活','','','2026-04-12','供给内容4','12','李娜','15187654321',4,4),(5,'2026-04-12 05:55:28','11','张强','13987654321','短视频剪辑','工具','upload/jiaohuanjilu_短视频剪辑1.jpg,upload/jiaohuanjilu_短视频剪辑2.jpg,upload/jiaohuanjilu_短视频剪辑3.jpg','工具','','','2026-04-12','供给内容5','11','张强','13987654321',5,5),(6,'2026-04-12 05:55:28','17','周凯','13812345678','硬笔书法','学习','upload/jiaohuanjilu_硬笔书法1.jpg,upload/jiaohuanjilu_硬笔书法2.jpg,upload/jiaohuanjilu_硬笔书法3.jpg','学习','','','2026-04-12','供给内容6','17','周凯','13812345678',6,6),(7,'2026-04-12 05:55:28','16','刘敏','15178901234','PPT美化设计','学习','upload/jiaohuanjilu_PPT美化设计1.jpg,upload/jiaohuanjilu_PPT美化设计2.jpg,upload/jiaohuanjilu_PPT美化设计3.jpg','学习','','','2026-04-12','供给内容7','16','刘敏','15178901234',7,7),(8,'2026-04-12 05:55:28','13','王浩','13290123456','吉他弹唱教学','艺术','upload/jiaohuanjilu_吉他弹唱教学1.jpg,upload/jiaohuanjilu_吉他弹唱教学2.jpg,upload/jiaohuanjilu_吉他弹唱教学3.jpg','艺术','','','2026-04-12','供给内容8','13','王浩','13290123456',8,8);
/*!40000 ALTER TABLE `jiaohuanjilu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiaohuanshenqing`
--

DROP TABLE IF EXISTS `jiaohuanshenqing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiaohuanshenqing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `xuqiubianhao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '需求编号',
  `yonghuzhanghao` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `shoujihao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `xuqiubiaoti` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '需求标题',
  `jinengfenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '技能分类',
  `fengmian` longtext COLLATE utf8mb4_unicode_ci COMMENT '封面',
  `shenqingshijian` datetime DEFAULT NULL COMMENT '申请时间',
  `shenqingbeizhu` longtext COLLATE utf8mb4_unicode_ci COMMENT '申请备注',
  `gongjirenzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '供给人账号',
  `gongjirenxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '供给人姓名',
  `lianxifangshi` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `xinyu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '信誉',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '审核回复',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交换申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiaohuanshenqing`
--

LOCK TABLES `jiaohuanshenqing` WRITE;
/*!40000 ALTER TABLE `jiaohuanshenqing` DISABLE KEYS */;
INSERT INTO `jiaohuanshenqing` VALUES (1,'2026-04-12 05:55:28','需求编号1','15','赵阳','13012345678','文言文翻译','学习','upload/jiaohuanshenqing_文言文翻译1.jpg,upload/jiaohuanshenqing_文言文翻译2.jpg,upload/jiaohuanshenqing_文言文翻译3.jpg','2026-04-12 13:55:28','申请备注1','15','赵阳','13012345678','信誉1','是',''),(2,'2026-04-12 05:55:28','需求编号2','18','吴芳','15065432109','摄影技巧教学','生活','upload/jiaohuanshenqing_摄影技巧教学1.jpg,upload/jiaohuanshenqing_摄影技巧教学2.jpg,upload/jiaohuanshenqing_摄影技巧教学3.jpg','2026-04-12 13:55:28','申请备注2','18','吴芳','15065432109','信誉2','是',''),(3,'2026-04-12 05:55:28','需求编号3','14','陈曦','13890123456','配音技巧教学','生活','upload/jiaohuanshenqing_配音技巧教学1.jpg,upload/jiaohuanshenqing_配音技巧教学2.jpg,upload/jiaohuanshenqing_配音技巧教学3.jpg','2026-04-12 13:55:28','申请备注3','14','陈曦','13890123456','信誉3','是',''),(4,'2026-04-12 05:55:28','需求编号4','12','李娜','15187654321','手工DIY教学','生活','upload/jiaohuanshenqing_手工DIY教学1.jpg,upload/jiaohuanshenqing_手工DIY教学2.jpg,upload/jiaohuanshenqing_手工DIY教学3.jpg','2026-04-12 13:55:28','申请备注4','12','李娜','15187654321','信誉4','是',''),(5,'2026-04-12 05:55:28','需求编号5','11','张强','13987654321','短视频剪辑','工具','upload/jiaohuanshenqing_短视频剪辑1.jpg,upload/jiaohuanshenqing_短视频剪辑2.jpg,upload/jiaohuanshenqing_短视频剪辑3.jpg','2026-04-12 13:55:28','申请备注5','11','张强','13987654321','信誉5','是',''),(6,'2026-04-12 05:55:28','需求编号6','17','周凯','13812345678','硬笔书法','学习','upload/jiaohuanshenqing_硬笔书法1.jpg,upload/jiaohuanshenqing_硬笔书法2.jpg,upload/jiaohuanshenqing_硬笔书法3.jpg','2026-04-12 13:55:28','申请备注6','17','周凯','13812345678','信誉6','是',''),(7,'2026-04-12 05:55:28','需求编号7','16','刘敏','15178901234','PPT美化设计','学习','upload/jiaohuanshenqing_PPT美化设计1.jpg,upload/jiaohuanshenqing_PPT美化设计2.jpg,upload/jiaohuanshenqing_PPT美化设计3.jpg','2026-04-12 13:55:28','申请备注7','16','刘敏','15178901234','信誉7','是',''),(8,'2026-04-12 05:55:28','需求编号8','13','王浩','13290123456','吉他弹唱教学','艺术','upload/jiaohuanshenqing_吉他弹唱教学1.jpg,upload/jiaohuanshenqing_吉他弹唱教学2.jpg,upload/jiaohuanshenqing_吉他弹唱教学3.jpg','2026-04-12 13:55:28','申请备注8','13','王浩','13290123456','信誉8','是',''),(9,'2026-04-12 06:17:01','1111111111','15','赵阳','13012345678','文言文翻译','学习','upload/jinengxuqiu_文言文翻译1.jpg','2026-04-12 14:16:50','','11','张强','13987654321','44','是','好的');
/*!40000 ALTER TABLE `jiaohuanshenqing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jinengfenlei`
--

DROP TABLE IF EXISTS `jinengfenlei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jinengfenlei` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `jinengfenlei` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '技能分类',
  PRIMARY KEY (`id`),
  UNIQUE KEY `jinengfenlei` (`jinengfenlei`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jinengfenlei`
--

LOCK TABLES `jinengfenlei` WRITE;
/*!40000 ALTER TABLE `jinengfenlei` DISABLE KEYS */;
INSERT INTO `jinengfenlei` VALUES (1,'2026-04-12 05:55:28','学习'),(2,'2026-04-12 05:55:28','生活'),(3,'2026-04-12 05:55:28','工具'),(4,'2026-04-12 05:55:28','艺术'),(5,'2026-04-12 05:55:28','技能分类5'),(6,'2026-04-12 05:55:28','技能分类6'),(7,'2026-04-12 05:55:28','技能分类7'),(8,'2026-04-12 05:55:28','技能分类8');
/*!40000 ALTER TABLE `jinengfenlei` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jinengxuqiu`
--

DROP TABLE IF EXISTS `jinengxuqiu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jinengxuqiu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `xuqiubianhao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '需求编号',
  `xuqiubiaoti` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '需求标题',
  `jinengfenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '技能分类',
  `fengmian` longtext COLLATE utf8mb4_unicode_ci COMMENT '封面',
  `fabushijian` date DEFAULT NULL COMMENT '发布时间',
  `xuqiumiaoshu` longtext COLLATE utf8mb4_unicode_ci COMMENT '需求描述',
  `xuqiuxiangqing` longtext COLLATE utf8mb4_unicode_ci COMMENT '需求详情',
  `yonghuzhanghao` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `shoujihao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `xinyuzhishu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '信誉指数',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '审核回复',
  `thumbsupnum` int(11) DEFAULT '0' COMMENT '赞',
  `crazilynum` int(11) DEFAULT '0' COMMENT '踩',
  `clicktime` datetime DEFAULT NULL COMMENT '最近点击时间',
  `clicknum` int(11) DEFAULT '0' COMMENT '点击次数',
  `discussnum` int(11) DEFAULT '0' COMMENT '评论数',
  `totalscore` double DEFAULT '0' COMMENT '评分',
  `storeupnum` int(11) DEFAULT '0' COMMENT '收藏数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xuqiubianhao` (`xuqiubianhao`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能需求';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jinengxuqiu`
--

LOCK TABLES `jinengxuqiu` WRITE;
/*!40000 ALTER TABLE `jinengxuqiu` DISABLE KEYS */;
INSERT INTO `jinengxuqiu` VALUES (1,'2026-04-12 05:55:28','1111111111','文言文翻译','学习','upload/jinengxuqiu_文言文翻译1.jpg,upload/jinengxuqiu_文言文翻译2.jpg,upload/jinengxuqiu_文言文翻译3.jpg','2026-04-12','精通古文语法与词汇，擅长文言文精准翻译与赏析','汉语言文学专业基础扎实，熟悉初高中及大学语文文言文篇目，可提供逐字逐句翻译与深度解读','15','赵阳','13012345678','29','是','',1,1,'2026-04-12 13:55:28',3,0,0,1),(2,'2026-04-12 05:55:28','2222222222','摄影技巧教学','生活','upload/jinengxuqiu_摄影技巧教学1.jpg,upload/jinengxuqiu_摄影技巧教学2.jpg,upload/jinengxuqiu_摄影技巧教学3.jpg','2026-04-12','精通人像、风光摄影构图与光影运用，熟悉相机操作','拥有单反相机使用经验，擅长校园人像、风景拍摄，可提供从拍摄到后期的全流程摄影指导服务','18','吴芳','15065432109','49','是','',2,2,'2026-04-12 13:55:28',2,0,0,2),(3,'2026-04-12 05:55:28','3333333333','配音技巧教学','生活','upload/jinengxuqiu_配音技巧教学1.jpg,upload/jinengxuqiu_配音技巧教学2.jpg,upload/jinengxuqiu_配音技巧教学3.jpg','2026-04-12','擅长不同风格配音，掌握气息控制与情感表达技巧','参与过校园广播剧、动画配音工作，熟悉配音设备使用，可根据文本内容指导情感表达与节奏把控','14','陈曦','13890123456','55','是','',3,3,'2026-04-12 13:55:28',3,0,0,3),(4,'2026-04-12 05:55:28','4444444444','手工DIY教学','生活','upload/jinengxuqiu_手工DIY教学1.jpg,upload/jinengxuqiu_手工DIY教学2.jpg,upload/jinengxuqiu_手工DIY教学3.jpg','2026-04-12','擅长折纸、黏土、编织等手工制作，可进行创意教学','拥有丰富的手工创作经验，擅长将校园元素融入手工制作，可指导完成节日礼物、文创产品等手工作品','12','李娜','15187654321','36','是','',4,4,'2026-04-12 13:55:28',4,0,0,4),(5,'2026-04-12 05:55:28','5555555555','短视频剪辑','工具','upload/jinengxuqiu_短视频剪辑1.jpg,upload/jinengxuqiu_短视频剪辑2.jpg,upload/jinengxuqiu_短视频剪辑3.jpg','2026-04-12','熟练运用剪辑软件，擅长节奏把控与特效制作','掌握剪映、PR等剪辑工具操作，熟悉校园短视频、vlog制作逻辑，可根据需求调整视频风格与时长','11','张强','13987654321','44','是','',5,5,'2026-04-12 13:55:28',5,0,0,5),(6,'2026-04-12 05:55:28','6666666666','硬笔书法','学习','upload/jinengxuqiu_硬笔书法1.jpg,upload/jinengxuqiu_硬笔书法2.jpg,upload/jinengxuqiu_硬笔书法3.jpg','2026-04-12','掌握楷书、行书规范书写技巧，可指导基础笔画、间架结构练习','熟练掌握硬笔书法5种字体书写技法，拥有校园书法比赛指导经验，可定制个性化练字方案','17','周凯','13812345678','24','是','',6,6,'2026-04-12 13:55:28',6,0,0,6),(7,'2026-04-12 05:55:28','7777777777','PPT美化设计','学习','upload/jinengxuqiu_PPT美化设计1.jpg,upload/jinengxuqiu_PPT美化设计2.jpg,upload/jinengxuqiu_PPT美化设计3.jpg','2026-04-12','擅长PPT逻辑梳理与视觉美化，提升演示效果','精通PPT设计技巧，熟悉学术汇报、校园活动等不同场景PPT制作要求，可快速提升PPT美观度与逻辑性','16','刘敏','15178901234','53','是','',7,7,'2026-04-12 13:55:28',7,0,0,7),(8,'2026-04-12 05:55:28','8888888888','吉他弹唱教学','艺术','upload/jinengxuqiu_吉他弹唱教学1.jpg,upload/jinengxuqiu_吉他弹唱教学2.jpg,upload/jinengxuqiu_吉他弹唱教学3.jpg','2026-04-12','擅长民谣、流行吉他弹唱，可进行零基础入门教学','拥有3年吉他演奏经验，熟悉零基础学员学习痛点，可定制循序渐进的教学计划，快速掌握弹唱技能','13','王浩','13290123456','58','是','',8,8,'2026-04-12 13:55:28',8,0,0,8);
/*!40000 ALTER TABLE `jinengxuqiu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '留言人id',
  `username` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '留言内容',
  `cpicture` longtext COLLATE utf8mb4_unicode_ci COMMENT '留言图片',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `rpicture` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留言反馈';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'2026-04-12 05:55:29',1,'用户名1','upload/messages_avatarurl1.jpg','留言内容1','upload/messages_cpicture1.jpg','回复内容1','upload/messages_rpicture1.jpg'),(2,'2026-04-12 05:55:29',2,'用户名2','upload/messages_avatarurl2.jpg','留言内容2','upload/messages_cpicture2.jpg','回复内容2','upload/messages_rpicture2.jpg'),(3,'2026-04-12 05:55:29',3,'用户名3','upload/messages_avatarurl3.jpg','留言内容3','upload/messages_cpicture3.jpg','回复内容3','upload/messages_rpicture3.jpg'),(4,'2026-04-12 05:55:29',4,'用户名4','upload/messages_avatarurl4.jpg','留言内容4','upload/messages_cpicture4.jpg','回复内容4','upload/messages_rpicture4.jpg'),(5,'2026-04-12 05:55:29',5,'用户名5','upload/messages_avatarurl5.jpg','留言内容5','upload/messages_cpicture5.jpg','回复内容5','upload/messages_rpicture5.jpg'),(6,'2026-04-12 05:55:29',6,'用户名6','upload/messages_avatarurl6.jpg','留言内容6','upload/messages_cpicture6.jpg','回复内容6','upload/messages_rpicture6.jpg'),(7,'2026-04-12 05:55:29',7,'用户名7','upload/messages_avatarurl7.jpg','留言内容7','upload/messages_cpicture7.jpg','回复内容7','upload/messages_rpicture7.jpg'),(8,'2026-04-12 05:55:29',8,'用户名8','upload/messages_avatarurl8.jpg','留言内容8','upload/messages_cpicture8.jpg','回复内容8','upload/messages_rpicture8.jpg');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `introduction` longtext COLLATE utf8mb4_unicode_ci COMMENT '简介',
  `picture` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布人',
  `headportrait` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `typename` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`),
  KEY `typename` (`typename`),
  CONSTRAINT `news_ibfk_1` FOREIGN KEY (`typename`) REFERENCES `newstype` (`typename`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告资讯';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (1,'2026-04-12 05:55:28','品牌战略调整公告','阐述品牌战略调整','upload/news_picture1.jpg','我司将对品牌战略进行调整，以适应市场变化','发布人1','upload/news_headportrait1.jpg','重大事项'),(2,'2026-04-12 05:55:28','数据备份完成通知','告知数据备份完成','upload/news_picture2.jpg','数据备份已顺利完成，可正常使用系统','发布人2','upload/news_headportrait2.jpg','维护通知'),(3,'2026-04-12 05:55:28','隐私政策变更通知','提示隐私政策变更','upload/news_picture3.jpg','隐私政策发生变更，请知悉','发布人3','upload/news_headportrait3.jpg','系统升级'),(4,'2026-04-12 05:55:29','系统故障修复通知','说明系统故障修复','upload/news_picture4.jpg','系统故障已修复，各项功能恢复正常','发布人4','upload/news_headportrait4.jpg','系统升级'),(5,'2026-04-12 05:55:29','服务器维护通知','通知服务器维护时间','upload/news_picture5.jpg','将于明日凌晨2点至5点对服务器进行维护，请提前做好数据备份','发布人5','upload/news_headportrait5.jpg','维护通知'),(6,'2026-04-12 05:55:29','新政策解读公告','解读新政策','upload/news_picture6.jpg','对最新发布的XX政策进行详细解读','发布人6','upload/news_headportrait6.jpg','重大事项'),(7,'2026-04-12 05:55:29','数据迁移通知','说明数据迁移相关事宜','upload/news_picture7.jpg','将于下周进行数据迁移，迁移期间部分功能可能受影响','发布人7','upload/news_headportrait7.jpg','维护通知'),(8,'2026-04-12 05:55:29','合作伙伴变更公告','公布合作伙伴变更','upload/news_picture8.jpg','与XX合作伙伴终止合作，同时新增XX合作方','发布人8','upload/news_headportrait8.jpg','重大事项');
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newstype`
--

DROP TABLE IF EXISTS `newstype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newstype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `typename` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`),
  KEY `newstype_gz94` (`typename`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资讯分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newstype`
--

LOCK TABLES `newstype` WRITE;
/*!40000 ALTER TABLE `newstype` DISABLE KEYS */;
INSERT INTO `newstype` VALUES (1,'2026-04-12 05:55:29','重大事项'),(2,'2026-04-12 05:55:29','维护通知'),(3,'2026-04-12 05:55:29','系统升级'),(4,'2026-04-12 05:55:29','系统升级'),(5,'2026-04-12 05:55:29','维护通知'),(6,'2026-04-12 05:55:29','重大事项'),(7,'2026-04-12 05:55:29','维护通知'),(8,'2026-04-12 05:55:29','重大事项');
/*!40000 ALTER TABLE `newstype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pingjiafankui`
--

DROP TABLE IF EXISTS `pingjiafankui`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pingjiafankui` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuzhanghao` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `shoujihao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `biaoti` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `jinengfenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '技能分类',
  `huifutupian` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复图片',
  `jiaohuanjineng` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交换技能',
  `pingjiashijian` date DEFAULT NULL COMMENT '评价时间',
  `jiaohuanbeizhu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交换备注',
  `jiaohuanrenzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交换人账号',
  `jiaohuanrenxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交换人姓名',
  `lianxifangshi` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `huifuneirong` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `pingjiajieguo` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评价结果',
  `huifushipin` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复视频',
  `huifufujian` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复附件',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '审核回复',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价反馈';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pingjiafankui`
--

LOCK TABLES `pingjiafankui` WRITE;
/*!40000 ALTER TABLE `pingjiafankui` DISABLE KEYS */;
INSERT INTO `pingjiafankui` VALUES (1,'2026-04-12 05:55:28','15','赵阳','13012345678','文言文翻译','学习','upload/pingjiafankui_文言文翻译1.jpg,upload/pingjiafankui_文言文翻译2.jpg,upload/pingjiafankui_文言文翻译3.jpg','学习','2026-04-12','交换备注1','15','赵阳','13012345678','回复内容1','好评','','','是',''),(2,'2026-04-12 05:55:28','18','吴芳','15065432109','摄影技巧教学','生活','upload/pingjiafankui_摄影技巧教学1.jpg,upload/pingjiafankui_摄影技巧教学2.jpg,upload/pingjiafankui_摄影技巧教学3.jpg','生活','2026-04-12','交换备注2','18','吴芳','15065432109','回复内容2','好评','','','是',''),(3,'2026-04-12 05:55:28','14','陈曦','13890123456','配音技巧教学','生活','upload/pingjiafankui_配音技巧教学1.jpg,upload/pingjiafankui_配音技巧教学2.jpg,upload/pingjiafankui_配音技巧教学3.jpg','生活','2026-04-12','交换备注3','14','陈曦','13890123456','回复内容3','好评','','','是',''),(4,'2026-04-12 05:55:28','12','李娜','15187654321','手工DIY教学','生活','upload/pingjiafankui_手工DIY教学1.jpg,upload/pingjiafankui_手工DIY教学2.jpg,upload/pingjiafankui_手工DIY教学3.jpg','生活','2026-04-12','交换备注4','12','李娜','15187654321','回复内容4','好评','','','是',''),(5,'2026-04-12 05:55:28','11','张强','13987654321','短视频剪辑','工具','upload/pingjiafankui_短视频剪辑1.jpg,upload/pingjiafankui_短视频剪辑2.jpg,upload/pingjiafankui_短视频剪辑3.jpg','工具','2026-04-12','交换备注5','11','张强','13987654321','回复内容5','好评','','','是',''),(6,'2026-04-12 05:55:28','17','周凯','13812345678','硬笔书法','学习','upload/pingjiafankui_硬笔书法1.jpg,upload/pingjiafankui_硬笔书法2.jpg,upload/pingjiafankui_硬笔书法3.jpg','学习','2026-04-12','交换备注6','17','周凯','13812345678','回复内容6','好评','','','是',''),(7,'2026-04-12 05:55:28','16','刘敏','15178901234','PPT美化设计','学习','upload/pingjiafankui_PPT美化设计1.jpg,upload/pingjiafankui_PPT美化设计2.jpg,upload/pingjiafankui_PPT美化设计3.jpg','学习','2026-04-12','交换备注7','16','刘敏','15178901234','回复内容7','好评','','','是',''),(8,'2026-04-12 05:55:28','13','王浩','13290123456','吉他弹唱教学','艺术','upload/pingjiafankui_吉他弹唱教学1.jpg,upload/pingjiafankui_吉他弹唱教学2.jpg,upload/pingjiafankui_吉他弹唱教学3.jpg','艺术','2026-04-12','交换备注8','13','王浩','13290123456','回复内容8','好评','','','是','');
/*!40000 ALTER TABLE `pingjiafankui` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensitivewords`
--

DROP TABLE IF EXISTS `sensitivewords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensitivewords` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `content` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '傻逼' COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='敏感词';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensitivewords`
--

LOCK TABLES `sensitivewords` WRITE;
/*!40000 ALTER TABLE `sensitivewords` DISABLE KEYS */;
INSERT INTO `sensitivewords` VALUES (1,'2026-04-12 05:55:29','傻逼');
/*!40000 ALTER TABLE `sensitivewords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storeup`
--

DROP TABLE IF EXISTS `storeup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storeup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `refid` bigint(20) DEFAULT NULL COMMENT '外键id',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表名',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `picture` longtext COLLATE utf8mb4_unicode_ci COMMENT '图片',
  `type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '类型',
  `inteltype` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storeup`
--

LOCK TABLES `storeup` WRITE;
/*!40000 ALTER TABLE `storeup` DISABLE KEYS */;
/*!40000 ALTER TABLE `storeup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syslog`
--

DROP TABLE IF EXISTS `syslog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `syslog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `operation` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户操作',
  `method` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求方法',
  `params` longtext COLLATE utf8mb4_unicode_ci COMMENT '请求参数',
  `time` bigint(20) DEFAULT NULL COMMENT '请求时长(毫秒)',
  `ip` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syslog`
--

LOCK TABLES `syslog` WRITE;
/*!40000 ALTER TABLE `syslog` DISABLE KEYS */;
/*!40000 ALTER TABLE `syslog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(500) NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='token表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,15,'11','yonghu','用户','gfz46ikga1k5wg6doekzjujk8pf1uguk','2026-04-12 06:16:49','2026-04-12 07:16:50');
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '管理员' COMMENT '角色',
  `image` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2026-04-12 05:55:29','admin','admin','管理员','upload/image1.jpg');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xuexiziliao`
--

DROP TABLE IF EXISTS `xuexiziliao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xuexiziliao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ziliaobiaoti` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资料标题',
  `ziliaoleixing` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资料类型',
  `ziliaofengmian` longtext COLLATE utf8mb4_unicode_ci COMMENT '资料封面',
  `ziliaoshipin` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资料视频',
  `ziliaofujian` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资料附件',
  `ziliaoxiangqing` longtext COLLATE utf8mb4_unicode_ci COMMENT '资料详情',
  `fabushijian` date DEFAULT NULL COMMENT '发布时间',
  `clicktime` datetime DEFAULT NULL COMMENT '最近点击时间',
  `clicknum` int(11) DEFAULT '0' COMMENT '点击次数',
  `discussnum` int(11) DEFAULT '0' COMMENT '评论数',
  `storeupnum` int(11) DEFAULT '0' COMMENT '收藏数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习资料';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xuexiziliao`
--

LOCK TABLES `xuexiziliao` WRITE;
/*!40000 ALTER TABLE `xuexiziliao` DISABLE KEYS */;
INSERT INTO `xuexiziliao` VALUES (1,'2026-04-12 05:55:28','UI原型快速设计','产品经理','upload/xuexiziliao_UI原型快速设计1.jpg,upload/xuexiziliao_UI原型快速设计2.jpg,upload/xuexiziliao_UI原型快速设计3.jpg','','','使用Figma、Axure工具快速绘制产品UI原型，标注交互逻辑与页面跳转关系，支撑产品需求评审','2026-04-12','2026-04-12 13:55:28',1,0,1),(2,'2026-04-12 05:55:28','Python自动化脚本编写','软件研发','upload/xuexiziliao_Python自动化脚本编写1.jpg,upload/xuexiziliao_Python自动化脚本编写2.jpg,upload/xuexiziliao_Python自动化脚本编写3.jpg','','','基于Python语言编写自动化爬虫、数据清洗脚本，实现重复工作流程的自动化执行，提升研发工作效率','2026-04-12','2026-04-12 13:55:28',2,0,2),(3,'2026-04-12 05:55:28','短视频脚本创作','新媒体运营','upload/xuexiziliao_短视频脚本创作1.jpg,upload/xuexiziliao_短视频脚本创作2.jpg,upload/xuexiziliao_短视频脚本创作3.jpg','','','根据平台算法与用户偏好，撰写具备起承转合结构的短视频脚本，包含镜头语言、台词设计，提升内容传播力','2026-04-12','2026-04-12 13:55:28',3,0,3),(4,'2026-04-12 05:55:28','市场竞品分析','市场营销','upload/xuexiziliao_市场竞品分析1.jpg,upload/xuexiziliao_市场竞品分析2.jpg,upload/xuexiziliao_市场竞品分析3.jpg','','','通过竞品调研、SWOT分析，梳理竞品优劣势与市场策略，制定差异化的产品推广方案','2026-04-12','2026-04-12 13:55:28',4,0,4),(5,'2026-04-12 05:55:28','职场压力管理','全行业','upload/xuexiziliao_职场压力管理1.jpg,upload/xuexiziliao_职场压力管理2.jpg,upload/xuexiziliao_职场压力管理3.jpg','','','学习时间管理、情绪调节方法，合理分配工作任务，缓解职场压力，保持高效工作状态','2026-04-12','2026-04-12 13:55:28',5,0,5),(6,'2026-04-12 05:55:28','直播控场互动技巧','直播电商','upload/xuexiziliao_直播控场互动技巧1.jpg,upload/xuexiziliao_直播控场互动技巧2.jpg,upload/xuexiziliao_直播控场互动技巧3.jpg','','','掌握直播节奏把控方法，运用互动话术、福利发放等手段提升直播间人气，促进产品转化','2026-04-12','2026-04-12 13:55:28',6,0,6),(7,'2026-04-12 05:55:28','财务报表解读','金融投资','upload/xuexiziliao_财务报表解读1.jpg,upload/xuexiziliao_财务报表解读2.jpg,upload/xuexiziliao_财务报表解读3.jpg','','','读懂资产负债表、利润表、现金流量表三大报表，分析企业偿债能力、盈利能力与运营效率，辅助投资决策','2026-04-12','2026-04-12 13:55:28',7,0,7),(8,'2026-04-12 05:55:28','供应链库存优化','制造业','upload/xuexiziliao_供应链库存优化1.jpg,upload/xuexiziliao_供应链库存优化2.jpg,upload/xuexiziliao_供应链库存优化3.jpg','','','基于销售数据预测库存需求，制定库存补货策略，降低库存积压与缺货风险，优化供应链成本','2026-04-12','2026-04-12 13:55:28',8,0,8);
/*!40000 ALTER TABLE `xuexiziliao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yonghu`
--

DROP TABLE IF EXISTS `yonghu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `yonghu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuzhanghao` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `mima` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `xingbie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `idcard` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `xinyuzhishu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '信誉指数',
  `shoujihao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `touxiang` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '审核回复',
  PRIMARY KEY (`id`),
  UNIQUE KEY `yonghuzhanghao` (`yonghuzhanghao`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yonghu`
--

LOCK TABLES `yonghu` WRITE;
/*!40000 ALTER TABLE `yonghu` DISABLE KEYS */;
INSERT INTO `yonghu` VALUES (11,'2026-04-12 05:55:28','15','赵阳','123456','女','610101200206066789',22,'29','13012345678','upload/yonghu_touxiang1.jpg','是',''),(12,'2026-04-12 05:55:28','18','吴芳','123456','男','210101197507077890',50,'49','15065432109','upload/yonghu_touxiang2.jpg','是',''),(13,'2026-04-12 05:55:28','14','陈曦','123456','女','330101199208088901',33,'55','13890123456','upload/yonghu_touxiang3.jpg','是',''),(14,'2026-04-12 05:55:28','12','李娜','123456','女','140101199902144567',30,'36','15187654321','upload/yonghu_touxiang4.jpg','是',''),(15,'2026-04-12 05:55:28','11','张强','123456','女','230101200304166789',26,'44','13987654321','upload/yonghu_touxiang5.jpg','是',''),(16,'2026-04-12 05:55:28','17','周凯','123456','男','420101200009099012',29,'24','13812345678','upload/yonghu_touxiang6.jpg','是',''),(17,'2026-04-12 05:55:28','16','刘敏','123456','女','430101199410100123',36,'53','15178901234','upload/yonghu_touxiang7.jpg','是',''),(18,'2026-04-12 05:55:28','13','王浩','123456','男','120101198605055678',38,'58','13290123456','upload/yonghu_touxiang8.jpg','是','');
/*!40000 ALTER TABLE `yonghu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ziliaoleixing`
--

DROP TABLE IF EXISTS `ziliaoleixing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ziliaoleixing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ziliaoleixing` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资料类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资料类型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ziliaoleixing`
--

LOCK TABLES `ziliaoleixing` WRITE;
/*!40000 ALTER TABLE `ziliaoleixing` DISABLE KEYS */;
INSERT INTO `ziliaoleixing` VALUES (1,'2026-04-12 05:55:28','产品经理'),(2,'2026-04-12 05:55:28','软件研发'),(3,'2026-04-12 05:55:28','新媒体运营'),(4,'2026-04-12 05:55:28','市场营销'),(5,'2026-04-12 05:55:28','全行业'),(6,'2026-04-12 05:55:28','直播电商'),(7,'2026-04-12 05:55:28','金融投资'),(8,'2026-04-12 05:55:28','制造业');
/*!40000 ALTER TABLE `ziliaoleixing` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-13 13:39:24
