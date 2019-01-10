CREATE DATABASE IF NOT EXISTS demo DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use demo;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  数据字典表
-- ----------------------------
DROP TABLE IF EXISTS `base_dict`;
CREATE TABLE `base_dict` (
  `code` varchar(100) NOT NULL COMMENT '编码',
  `pcode` varchar(100) NOT NULL COMMENT '父节点编码',
  `name_cn` varchar(200) NOT NULL COMMENT '中文名称',
  `name_en` varchar(200) DEFAULT NULL COMMENT '英文名称',
  `tips` varchar(2000) DEFAULT NULL COMMENT '提示',
  `order_num` int(11) DEFAULT 0 COMMENT '排序',
  `status` int DEFAULT 1 COMMENT '状态：0-禁用，1-启用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典表';

-- ----------------------------
--  初始化数据字典表
-- ----------------------------
INSERT INTO base_dict(code, pcode, name_cn, name_en, tips)
VALUES('ROOT', '', '根目录', 'ROOT', '根目录不能删除');

INSERT INTO base_dict(code, pcode, name_cn, name_en, tips)
VALUES('ACC_STATUS', 'ROOT', '账号状态', '', '测试数据可删除');

INSERT INTO base_dict(code, pcode, name_cn, name_en, tips)
VALUES('1', 'ACC_STATUS', '启用', '', '测试数据可删除');

INSERT INTO base_dict(code, pcode, name_cn, name_en, tips)
VALUES('0', 'ACC_STATUS', '禁用', '', '测试数据可删除');

commit;

SET FOREIGN_KEY_CHECKS = 1;

