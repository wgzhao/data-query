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
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`select_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `query_params`
(
    `select_id`   varchar(200) NOT NULL comment '查询ID，必须唯一',
    `param_name`  varchar(200) NOT NULL comment '参数名',
    `param_type`  varchar(10)  NOT NULL comment '参数类型，目前支持string,int,double,date,datetime,time,timestamp,decimal',
    `is_required` tinyint(1)   NOT NULL DEFAULT 1 comment '是否必须，默认为必须',
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `idx_query_params` (`select_id`, `param_name`, `param_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


create table IF NOT EXISTS data_sources
(
    no      varchar(10)  not null  primary key comment '数据源编号',
    name     varchar(200) null comment '数据源名称',
    url      varchar(200) not null comment '数据源URL,必须是jdbc:xxx://xxx:xxx/xxx',
    username varchar(100) null comment '数据源用户名',
    password varchar(200) null comment '数据源密码，可以为空',
    driver   varchar(200) not null comment '驱动类名',
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table if not exists signs
(
    app_id varchar(16)  not null comment 'app_id',
    app_key varchar(32) not null comment 'app_key',
    applier varchar(20) DEFAULT NULL COMMENT '申请人',
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (app_id)
) comment '签名表'
    ENGINE = InnoDB
    DEFAULT CHARSET = latin1;

create table if not exists query_logs
(
    id     bigint  not null auto_increment primary key comment 'logical ID',
    app_id varchar(16) not null comment 'app_id',
    select_id varchar(200) not null comment 'selectId',
    query_sql mediumtext not null comment 'execute SQL statement',
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) comment '查询日志表'
    ENGINE = MYISAM
    DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `query_config_sign` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `select_id` VARCHAR(200) NOT NULL,
    `app_id` VARCHAR(16) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- auth

create table if not exists users(
	username varchar(50) not null primary key,
	password varchar(500) not null,
    email   varchar(100) null ,
    role varchar(20) not null default 'USER',
	enabled boolean not null
);

create table if not exists authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index  ix_auth_username on authorities (username,authority);

-- initial admin with password admin123

insert into users values('admin', '{bcrypt}$2a$10$mT5cNJhoCFZkbYZAjA7NLOAb/vY2kx0wU.8pqSmXVk6EcKECpSydi', true);