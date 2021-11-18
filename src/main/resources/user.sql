CREATE TABLE `user` (
                        `uid` int(11) NOT NULL AUTO_INCREMENT,
                        `firstName` varchar(50) NOT NULL,
                        `lastName` varchar(50) NOT NULL,
                        `email` varchar(100) NOT NULL,
                        `password` varchar(32) NOT NULL,
                        `rol` enum('SELLER','CUSTOMER') NOT NULL,
                        `contact` varchar(50) DEFAULT NULL,
                        `birthday` datetime NOT NULL,
                        PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE `sakila`.`user`
    ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE;

ALTER TABLE `sakila`.`user`
    ADD COLUMN `photo` TEXT NULL AFTER `birthday`;

ALTER TABLE `sakila`.`user`
    CHANGE COLUMN `birthday` `birthday` DATETIME NOT NULL AFTER `rol`;



