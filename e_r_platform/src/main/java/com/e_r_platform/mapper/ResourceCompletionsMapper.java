package com.e_r_platform.mapper;

import com.e_r_platform.model.Resource;
import com.e_r_platform.model.ResourceCompletions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【resource_completions】的数据库操作Mapper
* @createDate 2024-04-27 15:52:23
* @Entity com.e_r_platform.model.ResourceCompletions
*/
public interface ResourceCompletionsMapper extends BaseMapper<ResourceCompletions> {
    int insertOne(@Param("resource_id") int resource_id, @Param("student_id") int student_id);

    ResourceCompletions getOneByResourceIDAndStudentID(@Param("resource_id") int resource_id, @Param("student_id") int student_id);

    ArrayList<Resource> getResourcesByCourseIDAndStudentID(@Param("course_id") int course_id,
                                                           @Param("student_id") int student_id);
}




