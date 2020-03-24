USE community;
CREATE TABLE lost_article (
  lost_article_id int(10) NOT NULL AUTO_INCREMENT,
  article_category_id int(10) NOT NULL ,
	user_id int(10) NOT NULL ,
	area_id int(5) NOT NULL ,
	item_category_id int(5) NOT NULL ,
	article_title varchar(200) NOT NULL,
	phone varchar(200) NOT NULL,
	article_content varchar(1024) NOT NULL,
	create_time datetime NOT NULL,
	finish_time datetime DEFAULT NULL,
	finisher_id int(10) DEFAULT NULL,
	primary key(lost_article_id),
foreign key (article_category_id) references article_category (article_category_id),
foreign key (owner_id) references user_info (user_id),
foreign key (area_id) references area (area_id),
foreign key (item_category_id) references item_category (item_category_id),
foreign key (finisher_id) references user_info (user_id)



)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;