-- james_user: table
CREATE TABLE `james_user` (
  `user_id`   int(11)     NOT NULL AUTO_INCREMENT,
  `creation`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP
  ON UPDATE CURRENT_TIMESTAMP,
  `pseudonym` varchar(60) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `james_user_user_id_uindex` (`user_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = latin1;

-- rank_indices: table
CREATE TABLE `rank_indices` (
  `rank_id`         int(11)     NOT NULL AUTO_INCREMENT,
  `simple_name`     varchar(60) NOT NULL,
  `teamspeak_group` int(11)     NOT NULL,
  PRIMARY KEY (`rank_id`),
  UNIQUE KEY `rank_indices_rank_uindex` (`simple_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- user_data: table
CREATE TABLE `user_data` (
  `user_id`      int(11) NOT NULL,
  `teamspeak_id` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_data_user_id_uindex` (`user_id`),
  UNIQUE KEY `user_data_teamspeak_id_uindex` (`teamspeak_id`),
  CONSTRAINT `user_data_james_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `james_user` (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- user_log: table
CREATE TABLE `user_log` (
  `log_id`      int(11)     NOT NULL AUTO_INCREMENT,
  `user_id`     int(11)     NOT NULL,
  `log_service` varchar(60) NOT NULL,
  `log_action`  varchar(60) NOT NULL,
  `log_result`  varchar(20) NOT NULL,
  PRIMARY KEY (`log_id`),
  KEY `user_log_james_user_user_id_fk` (`user_id`),
  CONSTRAINT `user_log_james_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `james_user` (`user_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = latin1;

-- No native definition for element:  user_log_james_user_user_id_fk: index
;

-- user_rank: table
CREATE TABLE `user_rank` (
  `user_id` int(11) NOT NULL,
  `rank_id` int(11) NOT NULL,
  UNIQUE KEY `user_rank_user_id_uindex` (`user_id`),
  KEY `user_rank_rank_indices_rank_id_fk` (`rank_id`),
  CONSTRAINT `user_rank_james_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `james_user` (`user_id`),
  CONSTRAINT `user_rank_rank_indices_rank_id_fk` FOREIGN KEY (`rank_id`) REFERENCES `rank_indices` (`rank_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

-- No native definition for element:  user_rank_rank_indices_rank_id_fk: index
;

