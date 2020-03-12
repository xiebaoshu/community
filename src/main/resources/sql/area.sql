USE community;
CREATE TABLE area(
  area_id int(10) NOT NULL AUTO_INCREMENT,
  area_name varchar(256) NOT NULL,
	primary key(area_id),
UNIQUE (area_name)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;