USE community;
CREATE TABLE item_category(
  item_category_id int(10) NOT NULL AUTO_INCREMENT,
  item_category_name varchar(256) NOT NULL,
	primary key(item_category_id),
UNIQUE (item_category_name)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;