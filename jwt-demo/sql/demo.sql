CREATE DATABASE IF NOT EXISTS demo DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use demo;

DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user
(
   account       VARCHAR(50) CHARSET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL COMMENT '账号',
   name          VARCHAR(50) CHARSET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL COMMENT '名字',
   department    VARCHAR(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '所属部门',
   login_status  BIT                                                       DEFAULT b'0' COMMENT '登陆状态, true:已登陆;false:未登录',
   password      VARCHAR(50) CHARSET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '密码'
)
ENGINE=InnoDB
COMMENT='系统用户表'
COLLATE=utf8mb4_general_ci;

