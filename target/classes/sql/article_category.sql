use community;
create table article_category
(
  article_category_id   int auto_increment primary key,
  article_category_name varchar(100) default ''  not null,
  parent_id          int                      null,
  foreign key (parent_id) references article_category (article_category_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



