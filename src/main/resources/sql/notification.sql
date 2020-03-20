CREATE TABLE notification (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`notifier_id`  int NOT NULL ,
`receiver_id`  int NOT NULL ,
`article_id`  int NOT NULL ,
`article_par_category`  int NOT NULL ,
`type`  int NOT NULL ,
`create_time`  datetime NOT NULL ,
`outerTitle`  varchar(255) NOT NULL ,
PRIMARY KEY (`id`),
CONSTRAINT `notifier_foreign` FOREIGN KEY (`notifier_id`) REFERENCES `user_info` (`user_id`),
CONSTRAINT `receiver_foreign` FOREIGN KEY (`receiver_id`) REFERENCES `user_info` (`user_id`),
CONSTRAINT `category_foreign` FOREIGN KEY (`article_par_category`) REFERENCES `article_category` (`article_category_id`)
)
;

