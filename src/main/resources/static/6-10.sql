-- expense_tracker.categories definition

CREATE TABLE `categories` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(20) NOT NULL,
                              `type` enum('expense','income') NOT NULL,
                              `icon` varchar(50) DEFAULT NULL,
                              `sort_order` int DEFAULT '0',
                              `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- expense_tracker.users definition

CREATE TABLE `users` (
                         `id` varchar(36) NOT NULL,
                         `username` varchar(50) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `avatar` varchar(255) DEFAULT NULL,
                         `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- expense_tracker.transactions definition

CREATE TABLE `transactions` (
                                `id` varchar(36) NOT NULL,
                                `user_id` varchar(36) NOT NULL,
                                `amount` decimal(12,2) NOT NULL,
                                `type_id` tinyint NOT NULL,
                                `category` varchar(20) NOT NULL,
                                `note` varchar(500) DEFAULT NULL,
                                `date` date NOT NULL,
                                `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                KEY `idx_user_date` (`user_id`,`date`),
                                KEY `idx_user_type` (`user_id`,`type_id`),
                                CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO expense_tracker.categories (name,`type`,icon,sort_order,created_at) VALUES
                                                                                    ('餐饮','expense','restaurant',1,'2026-06-10 15:06:31'),
                                                                                    ('交通','expense','directions_car',2,'2026-06-10 15:06:31'),
                                                                                    ('购物','expense','shopping_cart',3,'2026-06-10 15:06:31'),
                                                                                    ('住房','expense','home',4,'2026-06-10 15:06:31'),
                                                                                    ('娱乐','expense','movie',5,'2026-06-10 15:06:31'),
                                                                                    ('医疗','expense','local_hospital',6,'2026-06-10 15:06:31'),
                                                                                    ('教育','expense','school',7,'2026-06-10 15:06:31'),
                                                                                    ('通讯','expense','phone',8,'2026-06-10 15:06:31'),
                                                                                    ('服饰','expense','checkroom',9,'2026-06-10 15:06:31'),
                                                                                    ('美容','expense','face',10,'2026-06-10 15:06:31');
INSERT INTO expense_tracker.categories (name,`type`,icon,sort_order,created_at) VALUES
                                                                                    ('运动','expense','fitness_center',11,'2026-06-10 15:06:31'),
                                                                                    ('旅行','expense','flight',12,'2026-06-10 15:06:31'),
                                                                                    ('宠物','expense','pets',13,'2026-06-10 15:06:31'),
                                                                                    ('礼金','expense','card_giftcard',14,'2026-06-10 15:06:31'),
                                                                                    ('其他','expense','more_horiz',15,'2026-06-10 15:06:31'),
                                                                                    ('工资','income','account_balance',1,'2026-06-10 15:06:31'),
                                                                                    ('奖金','income','emoji_events',2,'2026-06-10 15:06:31'),
                                                                                    ('投资','income','trending_up',3,'2026-06-10 15:06:31'),
                                                                                    ('兼职','income','work',4,'2026-06-10 15:06:31'),
                                                                                    ('红包','income','card_giftcard',5,'2026-06-10 15:06:31');
INSERT INTO expense_tracker.categories (name,`type`,icon,sort_order,created_at) VALUES
                                                                                    ('退款','income','refresh',6,'2026-06-10 15:06:31'),
                                                                                    ('其他','income','more_horiz',7,'2026-06-10 15:06:31');
