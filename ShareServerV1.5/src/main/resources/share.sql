DROP DATABASE share;
CREATE DATABASE IF NOT EXISTS `share`;
USE share;

CREATE TABLE IF NOT EXISTS `user`
(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50),
  `password` VARCHAR(50),
  `name` VARCHAR(50),
  `phone` VARCHAR(12),
  `createTime` DATETIME,
  `loginTime` DATETIME,
  `state` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `course`
(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50),
  `createTime` DATETIME,
  `state` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `folder`
(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `course_id` INT(11),
  `user_id` INT(11),

  `title` VARCHAR(50),
  `content` VARCHAR(255),
  `path` VARCHAR(255),
  `fileName` VARCHAR(255),
  `createTime` DATETIME,
  `likeNum` INT NOT NULL DEFAULT 0,
  `dislikeNum` INT NOT NULL DEFAULT 0,
  `state` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `opinion`
(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `folder_id` INT(11),
  `user_id` INT(11),

  `content` VARCHAR(255),
  `attitude` INT NOT NULL DEFAULT 0,
  `createTime` DATETIME,
  `state` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
