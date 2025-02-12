package com.e_r_platform.mapper;

import com.e_r_platform.model.CourseStudents;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.e_r_platform.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【course_students】的数据库操作Mapper
* @createDate 2024-04-27 15:52:17
* @Entity com.e_r_platform.model.CourseStudents
*/
public interface CourseStudentsMapper extends BaseMapper<CourseStudents> {
    ArrayList<CourseStudents> getAllByCourseId(@Param("course_id") int course_id);

    ArrayList<CourseStudents> getAllByStudentId(@Param("student_id") int student_id);

    CourseStudents getOne(@Param("course_id") int course_id, @Param("user_id") int user_id);

    int addNewUserToCourse(@Param("course_id") int course_id, @Param("user_id") int user_id);

    int deleteUser(@Param("course_id") int course_id, @Param("user_id") int user_id);
}




