use e_r_platform;

# DROP TABLE IF EXISTS `resource_completions`;
#  DROP TABLE IF EXISTS `course_students`;
#  DROP TABLE IF EXISTS `resource`;
#  DROP TABLE IF EXISTS `course`;
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
    `introduction` varchar(511),
    `img_path` varchar(255),
    `teacher_id` int,
    primary key (`course_id`),
    foreign key (`teacher_id`) references `user`(`user_id`)
);

#  create table if not exists `resource`(
#      `resource_id` int not null auto_increment,
#      `course_id` int,
#      `name` varchar(255),
#      `path` varchar(255),
#      primary key (`resource_id`),
#      foreign key (`course_id`) references `course`(`course_id`)
# );

CREATE TABLE if not exists `resource` (
                             node_id INT PRIMARY KEY AUTO_INCREMENT,
                             course_id INT NOT NULL,
                             parent_node_id INT DEFAULT NULL COMMENT '顶级节点为null',
                             type ENUM('CHAPTER', 'LEAF') NOT NULL,
                             title VARCHAR(255) NOT NULL,
                             file_uri VARCHAR(511) DEFAULT NULL COMMENT '仅叶子节点需要',
                             display_order INT NOT NULL DEFAULT 0,
                             is_folder BOOLEAN GENERATED ALWAYS AS (type = 'CHAPTER') VIRTUAL,

                             FOREIGN KEY (course_id) REFERENCES course(course_id),
                             FOREIGN KEY (parent_node_id) REFERENCES resource(node_id)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
#  索引配置
CREATE INDEX idx_course_tree ON resource(course_id, parent_node_id, display_order);

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
    foreign key (`resource_id`) references `resource`(`node_id`),
    foreign key (`student_id`) references `user`(`user_id`)
);

