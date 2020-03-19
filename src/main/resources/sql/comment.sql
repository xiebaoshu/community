use community;
create table comment
(
  comment_id   BIGINT auto_increment primary key,
  article_id int not null,
  user_id   int not null,
  content varchar(100) default ''  not null,
  parent_id     BIGINT    null,
  is_admin boolean not null ,
  create_time date not null ,

  foreign key (parent_id) references comment (comment_id),
  foreign key (article_id) references lost_article (lost_article_id),
  foreign key (user_id) references user_info (user_id)
)ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



