/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50041
Source Host           : localhost:3306
Source Database       : moji

Target Server Type    : MYSQL
Target Server Version : 50041
File Encoding         : 65001

Date: 2019-12-17 15:07:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `comment_detail`
-- ----------------------------
DROP TABLE IF EXISTS `comment_detail`;
CREATE TABLE `comment_detail` (
  `comment_id` varchar(30) NOT NULL,
  `note_id` varchar(20) NOT NULL,
  `comment_user_id` varchar(20) NOT NULL,
  `comment_content` varchar(200) default NULL,
  `comment_time` varchar(30) default NULL,
  PRIMARY KEY  (`comment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment_detail
-- ----------------------------
INSERT INTO `comment_detail` VALUES ('171828121622231445_7939', '121622231445_7939', '677_9781', '哈哈哈', '2019/12/17');
INSERT INTO `comment_detail` VALUES ('171255121710225145_7939', '121710225145_7939', '45_7939', '还回家', '2019/12/17');
INSERT INTO `comment_detail` VALUES ('171124121710225145_7939', '121710225145_7939', '45_7939', '', '2019/12/17');
INSERT INTO `comment_detail` VALUES ('175531121622595745_7939', '121622595745_7939', '1234', '???', '2019/12/17');
INSERT INTO `comment_detail` VALUES ('161714121518080245_7939', '121518080245_7939', '45_7939', '哈哈哈红红火火恍恍惚惚哈哈哈哈', '2019/12/16');
INSERT INTO `comment_detail` VALUES ('161936121518080245_7939', '121518080245_7939', '45_7939', '啊啊啊啊啊啊~啊啊啊啊啊啊啊啊啊啊啊', '2019/12/16');
INSERT INTO `comment_detail` VALUES ('163913121518080245_7939', '121518080245_7939', '45_7939', '反反复复', '2019/12/16');
INSERT INTO `comment_detail` VALUES ('164046121518080245_7939', '121518080245_7939', '1234', '啦啦啦啦啦', '2019/12/16');
INSERT INTO `comment_detail` VALUES ('162359121622231445_7939', '121622231445_7939', '45_7939', '噢噢噢哦哦来了', '2019/12/16');
INSERT INTO `comment_detail` VALUES ('160018121622595745_7939', '121622595745_7939', '45_7939', '啥', '2019/12/16');

-- ----------------------------
-- Table structure for `mail_msg`
-- ----------------------------
DROP TABLE IF EXISTS `mail_msg`;
CREATE TABLE `mail_msg` (
  `mail_id` int(11) NOT NULL auto_increment,
  `acceptTime` varchar(20) default NULL,
  `comment_content` varchar(200) default NULL,
  `otherName` varchar(20) default NULL,
  `userId` varchar(20) default NULL,
  PRIMARY KEY  (`mail_id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_msg
-- ----------------------------
INSERT INTO `mail_msg` VALUES ('7', '2019/12/16', '啥', '晚晚晚晚', '45_7939');
INSERT INTO `mail_msg` VALUES ('5', '2019/12/16', '啦啦啦啦啦', '行百里者半九十', '45_7939');
INSERT INTO `mail_msg` VALUES ('8', '2019/12/17', '???', '行百里者半九十', '45_7939');
INSERT INTO `mail_msg` VALUES ('9', '2019/12/17', '', '天若微凉', '45_7939');
INSERT INTO `mail_msg` VALUES ('10', '2019/12/17', '还回家', '天若微凉', '45_7939');
INSERT INTO `mail_msg` VALUES ('11', '2019/12/17', '哈哈哈', '677_9781', '45_7939');

-- ----------------------------
-- Table structure for `moji_ana`
-- ----------------------------
DROP TABLE IF EXISTS `moji_ana`;
CREATE TABLE `moji_ana` (
  `id` int(10) NOT NULL auto_increment,
  `title` varchar(20) character set utf8 default 'null',
  `content` varchar(500) character set utf8 default 'null',
  `time` varchar(20) character set utf8 default NULL,
  `place` varchar(20) character set utf8 default 'null',
  `image` varchar(255) character set utf8 default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of moji_ana
-- ----------------------------
INSERT INTO `moji_ana` VALUES ('5', '善良最能温暖世道', '善良是挂在心底的一轮澄澈的明月，它照亮的，是一个人精神的天空。一个一辈子行善的人，心底的月亮，已经超越了个人，升起在尘世寥廓的江天之上。它洞照的，是这个世界所有人的良心，以及灵魂的纯度。', '2019/12/15', '河北师范大学', 'images/timg7.jpg');
INSERT INTO `moji_ana` VALUES ('13', '心静则神凝', '人无论再怎么被爱，都会挂念那些没有得到的爱。影响一个人的，不是到手的东西，而是求之不得的东西。', '2019/12/15', '河北师范大学', 'images/timg1.jpg');
INSERT INTO `moji_ana` VALUES ('33', '当局者迷', '当我们看世界的时候，总是以为自己站在宇宙的中心，认为所观察的一切如此全面而正确，却忘记了，最大的盲点，其实就是站在中心的自己。', '2019/12/15', '河北师范大学', 'images/timg2.jpg');
INSERT INTO `moji_ana` VALUES ('34', '(⊙o⊙)…', '做人最糟糕的有两点。第一是不信守承诺，答应别人的事，用一句我也没办法就搪塞了。第二是小有成就就飘飘然。前者害朋友，后者害自己。所以前者不可信，后者不可交。', '2019/12/15', '河北师范大学', 'images/timg3.jpg');
INSERT INTO `moji_ana` VALUES ('35', '月上柳梢头，人约黄昏后', '人生不过是午后到黄昏的距离，茶凉言尽，月上柳梢。', '2019/12/15', '河北师范大学', 'images/timg4.jpg');
INSERT INTO `moji_ana` VALUES ('37', '渡人先渡己，渡己先渡心', '深山的鹿，不知归处；万般皆苦，只可自渡。', '2019/12/15', '河北师范大学', 'images/timg5.jpg');
INSERT INTO `moji_ana` VALUES ('38', '心有阳光，温暖如诗', '天气越来越冷，愿你对生活依然热情；世界偶尔薄情，愿你一如既往深情。心有阳光，把生活过成诗，找到温暖自己的方式。', '2019/12/15', '河北师范大学', 'images/timg6.jpg');
INSERT INTO `moji_ana` VALUES ('40', '一往无前', '希望每个整装待发的重新开始，都在这年少的岁月里为时不晚。', '2019/12/15', '河北师范大学', 'images/timg8.jpg');

-- ----------------------------
-- Table structure for `note_detail`
-- ----------------------------
DROP TABLE IF EXISTS `note_detail`;
CREATE TABLE `note_detail` (
  `note_id` varchar(20) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `title` varchar(20) default NULL,
  `content` varchar(400) default NULL,
  `latitude` double default NULL,
  `longitude` double default NULL,
  `location` varchar(30) default NULL,
  `time` varchar(30) default NULL,
  `self` int(11) default NULL,
  PRIMARY KEY  (`note_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of note_detail
-- ----------------------------
INSERT INTO `note_detail` VALUES ('191211123', '1234', '抱犊寨一日游', '    趁着天清气朗，寻思着到石家庄郊外游览一番，通过高徳地图查看周边景点，显示抱犊寨排在热门景点第一位，又只有不到20公里路程，相比不是很远，就决定去看看。\r\n\r\n    2016年9月12日早上7点左右从太行国宾馆坐325路公交车到横山转320路，花了一个来小时到达抱犊寨，山下售票窗口还未开，因此跟着人群沿着石级往上爬。\r\n\r\n    上山的路坡度较大，相比较陡，加之沿途树阴不多，太阳直晒下，没爬几步就大汗淋漓。因为比较早，上山人还不多，不过已有下山的了，真的是“莫道君行早，更有早行人。”啦。北方的山没有南方那么葱翠秀丽，但较多裸露的岩石也彰显出几分雄壮。沿着石梯爬到顶上才知当前所爬的山应该是莲花山，对面那座山顶较平、四面峭壁才是抱犊寨所在。看着长长的蜿蜒向上的山道，看来又要出一身汗了。原以为沿途红顶房子建造精致，到跟前才知只是摆摊卖东西的简陋棚子而已，几根柱子支撑，感觉不结实也不抗风', '38.094246', '114.283595', '石家庄市·抱犊寨', '2019-12-11 19:38:18', '0');
INSERT INTO `note_detail` VALUES ('191211234', '2345', '小故事', '         有个小村庄里有位中年邮差，他从刚满二十岁起便开始每天往返五十公里的路程，日复一日将忧欢悲喜的故事，送到居民的家中。就这样二十年一晃而过，人事物几番变迁，唯独从邮局到村庄的这条道路，从过去到现在，始终没有一枝半叶，触目所及，唯有飞扬的尘土罢了。“这样荒凉的路还要走多久呢？”他一想到必须在这无花无树充满尘土的路上，踩着脚踏车度过他的人生时，心中总是有些遗憾。有一天当他送完信，心事重重准备回去时，刚好经过了一家花店。“对了，就是这个！”\r\n　　他走进花店，买了一把野花的种子，并且从第二天开始，带着这些种子撒在往来的路上。\r\n　　就这样，经过一天，两天，一个月，两个月……，他始终持续散播着野花种子。\r\n　　没多久，那条已经来回走了二十年的荒凉道路，竟开起了许多红、黄各色的小花；夏天开夏天的花，秋天开秋天的花，四季盛开，永不停歇。\r\n　　种子和花香对村庄里的人来说，比有个小村庄里有', '37.982107', '114.528177', '石家庄市·河北科技大学-图书馆', '2019-12-10 10:06:29', '0');
INSERT INTO `note_detail` VALUES ('191211456', '3456', '随笔', '    立在窗前，点了支烟，目光沿着窗外的车水马龙，花草树木。回想着旧日的时光，感悟过往，叹惋岁月匆忙，那些被琐事浸染的日子。生命中有许多东西，往往出现在意料之外，而又在意料之中，然后兀自远去，我徒生些许伤感。', '38.001909', '114.525969', '石家庄市·河北师范大学-公教楼B座', '2019-09-10 10:06:29', '0');
INSERT INTO `note_detail` VALUES ('191211567', '4567', '随笔', '    随着年龄都增长，渐渐对自己的生日没有那么大感触了。不像小时候，很执着于谁给了自己祝福，甚至还会在意谁先给自己祝福。仿佛越快给你生日祝福的人，就表示越在乎你。\r\n\r\n　　为什么会开始不那么在意了呢？容我想想。也许是明白了，每个人都有自己的生活，每个人也有不一样表达爱与关怀的方式。不要用自以为是的标准，来衡量其他人的爱。', '38.051885', '114.541119', '石家庄市·河北医科大', '2019-10-23 10:06:29', '0');
INSERT INTO `note_detail` VALUES ('191212123', '5678', '随笔', '    每个人生活在这个世界上，都有他特定的角色，这个角色在人生的每个阶段都是不一样的，角色决定了阶段所承担的责任和义务。每个人都想活的潇潇洒洒，每个人都顺心而为，每个人都想自己能主宰一切，但生活往往反其道行，可能这就是每个人生活在这个地球上的价值所在吧。', '38.089425', '114.515029', '石家庄市·石家庄铁道大学', '2019-12-10 10:06:29', '0');
INSERT INTO `note_detail` VALUES ('121710225145_7939', '45_7939', '早上好', 'Good Morning!', '37.997936', '114.52259', '石家庄市.河北师范大学(新校区)', '2019-12-17 10:22:51', '0');
INSERT INTO `note_detail` VALUES ('121622231445_7939', '45_7939', '随笔', '    大学里，遇见的人多了，看到得事也多了，所以，自己的想法也多了。大学是社会的缩影，反应着这个真实的世界，让我们更好的生存在这个社会。在这里我们会遇到许多的人和事，我们也会在这里潜移默化的改变自己。这是一个必须要经历了的过程，当然我们也可以坚持着自己的理念，不改变、不为外物所动。但现实和我们的想法总是背道而驰的，什么事情都是事与愿违。要我们坚持自己观念不改变是一件很困难的事情，因为我们生活在大学这个群落里，身边的人总是会再不经意间改变着我们的观念。但我们不必害怕，也许这不会是一件坏事呢？它可以让自己的观念跟上时代发展的脚步，为时代的发展做出贡献。任何事情都有它的两面性，我们看到好的一面，也要看到坏的一面。', '38.003104', '114.524332', '石家庄市.蓝湾家园', '2019-12-16 22:23:14', '0');
INSERT INTO `note_detail` VALUES ('121622595745_7939', '45_7939', '随笔', '    大一已经过去3个月的时间。早在开学，老师就说过，大学与中学相比会增加更多考验、提升能力的机会，而不单单是学习。\n\n    我回头看看，三个月来，虽有迷茫但所幸这样的日子不多――因为开学前，关心我的老师就说过“多看看书，书里有很多问题答案和睿智的话”――而我也确实这样做了，在心情刚刚跌到冰点的时候，我去图书馆看励志故事、跟南开大学毕业的叔叔沟通交流，而正因如此，我慢慢地走出了17岁大一生活的阴雨天。', '37.997631', '114.514009', '石家庄市·河北师范大学-启智园', '2019-12-16 22:59:57', '0');

-- ----------------------------
-- Table structure for `note_img`
-- ----------------------------
DROP TABLE IF EXISTS `note_img`;
CREATE TABLE `note_img` (
  `img_id` varchar(20) NOT NULL default '0',
  `path` varchar(100) default NULL,
  `noteId` varchar(20) default NULL,
  PRIMARY KEY  (`img_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of note_img
-- ----------------------------
INSERT INTO `note_img` VALUES ('1912102344', 'image/7.jpg', '191211456');
INSERT INTO `note_img` VALUES ('121619120245_79390', 'image/pic6.jpg', '121619120245_7939');
INSERT INTO `note_img` VALUES ('1912108900', 'image/6.jpg', '191211456');
INSERT INTO `note_img` VALUES ('1912128921', 'image/4.jpg', '191212123');
INSERT INTO `note_img` VALUES ('1912123454', 'image/2.jpg', '191212123');
INSERT INTO `note_img` VALUES ('1912127890', 'image/3.jpg', '191212123');
INSERT INTO `note_img` VALUES ('1912111344', 'image/b3.jpg', '191211123');
INSERT INTO `note_img` VALUES ('1912111231', 'image/b4.jpg', '191211123');
INSERT INTO `note_img` VALUES ('1912111823', 'image/b2.jpg', '191211123');
INSERT INTO `note_img` VALUES ('1912111230', 'image/b1.jpg', '191211123');
INSERT INTO `note_img` VALUES ('121622231445_79390', 'image/pic6.jpg', '121622231445_7939');
INSERT INTO `note_img` VALUES ('19121510180012340', 'image/pic1.jpg', '1912151018001234');
INSERT INTO `note_img` VALUES ('121517090645_79390', 'image/pic6.jpg', '121517090645_7939');
INSERT INTO `note_img` VALUES ('121518080245_79390', 'image/pic1.jpg', '121518080245_7939');
INSERT INTO `note_img` VALUES ('121622595745_79390', 'image/img3.jpg', '121622595745_7939');
INSERT INTO `note_img` VALUES ('121622595745_79391', 'image/img4.jpg', '121622595745_7939');
INSERT INTO `note_img` VALUES ('121710225145_79390', 'image/img-90ae4b089fc249a6f46091b6ce4b8f66.jpg', '121710225145_7939');
INSERT INTO `note_img` VALUES ('121710225145_79391', 'image/img-c880641f513d9cfefe4e66bc0f529112.jpg', '121710225145_7939');

-- ----------------------------
-- Table structure for `user_detail`
-- ----------------------------
DROP TABLE IF EXISTS `user_detail`;
CREATE TABLE `user_detail` (
  `user_id` varchar(20) NOT NULL default '',
  `avatar_path` varchar(50) default 'avatar/default_head.png',
  `user_name` varchar(20) default NULL,
  `sex` varchar(10) default 'boy',
  `signature` varchar(300) default NULL,
  `occupation` varchar(10) default NULL,
  `password` varchar(10) default NULL,
  `phone` varchar(11) default NULL,
  PRIMARY KEY  (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_detail
-- ----------------------------
INSERT INTO `user_detail` VALUES ('1234', 'avatar/head3.jpg', '行百里者半九十', 'boy', '世界上最遥远的距离不是生和死的距离，而是我刚联机的那一秒，你却脱机了。', '学生', '111111', '15133170000');
INSERT INTO `user_detail` VALUES ('2345', 'avatar/head2.jpg', '向着太阳迎着光', 'girl', '每天把牢骚拿出来晒晒太阳，心情就不会缺钙。', '学生', '222222', '13444670000');
INSERT INTO `user_detail` VALUES ('3456', 'avatar/head4.jpg', 'TheCoolDay', 'boy', '生活就像海洋，只有意志坚强的人，才能到达彼岸', '学生', '333333', '15055670000');
INSERT INTO `user_detail` VALUES ('4567', 'avatar/head1.jpg', '奋斗べ青年00', 'girl', '始终相信，平时日子里做过的小小的善良的事，会一点一点积攒，然后在某一个不太明朗的时候，还给我一个大大的温柔。', '学生', '444444', '15244460000');
INSERT INTO `user_detail` VALUES ('5678', 'avatar/head5.jpg', '一苇以航', 'boy', '要好好努力了', '学生', '555555', '15822240000');
INSERT INTO `user_detail` VALUES ('45_7939', 'avatar/pic1.jpg', '天若微凉', 'girl', '神', '学生', '123456', '15175187939');
INSERT INTO `user_detail` VALUES ('345_2551', 'avatar/default_head.png', '345_2551', 'boy', null, null, '666666', '15633112551');
INSERT INTO `user_detail` VALUES ('677_9781', 'avatar/1566560197770.JPG', 'Myrle', 'girl', '', '学生', '123456', '18731179781');