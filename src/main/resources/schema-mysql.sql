CREATE DATABASE IF NOT EXISTS data_query;

USE data_query;

CREATE TABLE IF NOT EXISTS `query_config`
(
    `select_id`    varchar(200) NOT NULL comment '查询ID，必须唯一',
    `query_sql`    text         NOT NULL comment '查询SQL语句',
    `data_source`  varchar(200) NOT NULL DEFAULT 'trino' comment '查询的数据源，目前支持presto,allsql,phoenix',
    `cache_time`   int(11)      NOT NULL DEFAULT 60 comment '缓存时间，单位秒，当 enable_cache 为 0 ，改配置无效',
    `note`         varchar(200)          DEFAULT NULL comment '备注',
    `is_enable`    tinyint(1)   NOT NULL DEFAULT 1 comment '是否启用，默认为启用',
    `enable_cache` tinyint(1)   NOT NULL DEFAULT 1 comment '是否启用缓存，默认为启用',
    PRIMARY KEY (`select_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `query_params`
(
    `select_id`   varchar(200) NOT NULL comment '查询ID，必须唯一',
    `param_name`  varchar(200) NOT NULL comment '参数名',
    `param_type`  varchar(10)  NOT NULL comment '参数类型，目前支持string,int,double,date,datetime,time,timestamp,decimal',
    `is_required` tinyint(1)   NOT NULL DEFAULT 1 comment '是否必须，默认为必须',
    UNIQUE KEY `idx_query_params` (`select_id`, `param_name`, `param_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


create table IF NOT EXISTS data_sources
(
    name     varchar(200) not null comment '数据源名称',
    url      varchar(200) not null comment '数据源URL,必须是jdbc:xxx://xxx:xxx/xxx',
    username varchar(100) not null comment '数据源用户名',
    password varchar(200) null comment '数据源密码，可以为空',
    driver   varchar(200) not null comment '驱动类名',
    primary key (name)
);