package com.e_r_platform.mapper;

import com.e_r_platform.model.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【course】的数据库操作Mapper
* @createDate 2024-04-27 15:52:13
* @Entity com.e_r_platform.model.Course
*/
public interface CourseMapper extends BaseMapper<Course> {
    ArrayList<Course> getAll();

    Course getOne(@Param("course_id")int course_id);

    Course searchByNameAndTeacherId(@Param("name") String name, @Param("teacher_id") int teacher_id);

    int create(@Param("name") String name, @Param("teacher_id") int teacher_id);

    int deleteByCourse_id(@Param("course_id") int course_id);

    int update(@Param("course_id") int course_id, @Param("name") String name, @Param("introduction") String introduction, @Param("teacher_id") int teacher_id);
}




