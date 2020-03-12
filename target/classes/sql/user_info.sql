use community;
CREATE TABLE user_info
(
  user_id   int(100) NOT NULL AUTO_INCREMENT,
  user_account varchar(100) default ''  not null,
  user_password varchar(100) default ''  not null,
  user_name  varchar(100) default ''  not null,
  gender         varchar(2)  not null,
  user_class  varchar(100) default ''  not null,
  user_img    varchar(1024)      null,
  user_type      int(2) default '0' not null,
  permission     int(2) default '1' not null,
	primary key(user_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



