CREATE TABLE IF NOT EXISTS `query_config`
(
    `select_id`    varchar(200) NOT NULL ,
    `query_sql`    text         NOT NULL ,
    `data_source`  varchar(200) NOT NULL ,
    `cache_time`   int(11)      NOT NULL DEFAULT 60 ,
    `note`         varchar(200)          DEFAULT NULL,
    `is_enable`    boolean   NOT NULL DEFAULT true ,
    `enable_cache` boolean   NOT NULL DEFAULT true ,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (`select_id`)
) ;

CREATE TABLE IF NOT EXISTS `query_params`
(
    `select_id`   varchar(200) NOT NULL ,
    `param_name`  varchar(200) NOT NULL ,
    `param_type`  varchar(10)  NOT NULL,
    `is_required` boolean   NOT NULL DEFAULT true,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ,
    UNIQUE KEY `idx_query_params` (`select_id`, `param_name`, `param_type`)
);


create table IF NOT EXISTS data_sources
(
    no      varchar(10)  not null  primary key,
    name     varchar(200) null ,
    url      varchar(1000) not null ,
    username varchar(100) null,
    password varchar(200) null ,
    driver   varchar(200) not null,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP
);

create table if not exists signs
(
    app_id varchar(16)  not null,
    app_key varchar(32) not null ,
    applier varchar(20) DEFAULT NULL,
    enabled boolean not null default true,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP,
    primary key (app_id)
);

create table if not exists query_logs
(
    id     bigint  not null ,
    app_id varchar(16) not null,
    select_id varchar(200) not null ,
    query_sql mediumtext not null,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP
);

-- auth
create table if not exists users(
	username varchar(50) not null primary key,
	password varchar(500) not null,
    email   varchar(100) null ,
	enabled boolean not null default true
);

create table if not exists authorities (
     username varchar_ignorecase(50) not null,
     authority varchar_ignorecase(50) not null,
     constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);
