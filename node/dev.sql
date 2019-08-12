#查看用户
select current_user() from dual;

#新建用户
CREATE USER 'yy_sport'@'%' IDENTIFIED BY 'select0510';
#创建数据库
CREATE DATABASE selectdb;
grant all privileges on yy_sportdb.* to 'dba'@'%' identified by 'select0510';
#修改密码
SET PASSWORD FOR 'yy_sport'@'%' = PASSWORD('select0510');
flush privileges;
#修改当前登录用户
SET PASSWORD = PASSWORD("select0510");
#撤销用户权限
EVOKE privilege ON databasename.tablename FROM 'username'@'host';
#修改数据库enterprises的字符集：
alter database enterprises character set utf8

#修改数据表employees的字符集：

alter table employees character set utf8

修改字段的字符集

alter table employees change name name char(10) character set utf-8;
-- auto-generated definition
create table users
(
  id       int(10) auto_increment comment '主键'
    primary key,
  username varchar(50)  not null comment '用户名',
  password varchar(40)  not null comment '密码',
  jwt      varchar(250) null comment 'javaWebToken字符串'
);


#修改服务器编码
set @@character_set_server='utf8';

























