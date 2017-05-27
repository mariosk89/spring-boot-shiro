drop table if exists users;
drop table if exists user_roles;
drop table if exists roles_permissions;

create table if not exists users (
  id int NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY ,
  username varchar(256) NOT NULL UNIQUE,
  password varchar(256) NOT NULL,
  enabled boolean
);

create table if not exists user_roles (
  username varchar(256) NOT NULL UNIQUE,
  role_name varchar(256) NOT NULL
);

create table if not exists roles_permissions (
  role_name varchar(256) NOT NULL UNIQUE,
  permission varchar(256) NOT NULL
);
