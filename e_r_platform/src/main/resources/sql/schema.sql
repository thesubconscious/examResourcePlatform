use e_r_platform;

# DROP TABLE IF EXISTS `resource_completions`;
# DROP TABLE IF EXISTS `course_students`;
# DROP TABLE IF EXISTS `resource`;
# DROP TABLE IF EXISTS `course`;
# DROP TABLE IF EXISTS `user`;

create table if not exists `user`(
    `user_id` int not null auto_increment,
    `name` varchar(255),
    `email` varchar(255),
    `password` varchar(255),
    `identity` varchar(255),
    primary key (`user_id`)
);

create table if not exists `course`(
    `course_id` int not null auto_increment,
    `name` varchar(255),
    `teacher_id` int,
    primary key (`course_id`),
    foreign key (`teacher_id`) references `user`(`user_id`)
);

create table if not exists `resource`(
    `resource_id` int not null auto_increment,
    `course_id` int,
    `name` varchar(255),
    `path` varchar(255),
    primary key (`resource_id`),
    foreign key (`course_id`) references `course`(`course_id`)
);

create table if not exists `course_students`(
    `course_id` int,
    `student_id` int,
    foreign key (`course_id`) references `course`(`course_id`),
    foreign key (`student_id`) references `user`(`user_id`)
);

create table if not exists `resource_completions`(
    `resource_id` int,
    `student_id` int,
    `completion_time` datetime,
    foreign key (`resource_id`) references `resource`(`resource_id`),
    foreign key (`student_id`) references `user`(`user_id`)
);

