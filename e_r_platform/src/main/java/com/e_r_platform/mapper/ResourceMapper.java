package com.e_r_platform.mapper;

import com.e_r_platform.model.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【resource】的数据库操作Mapper
* @createDate 2024-04-27 15:52:20
* @Entity com.e_r_platform.model.Resource
*/
public interface ResourceMapper extends BaseMapper<Resource> {
    int insertOne(@Param("course_id") int course_id, @Param("name") String name, @Param("path") String path);

    int deleteByResourceId(@Param("resource_id") int resource_id);

    Resource searchAllByResourceId(@Param("resource_id") int resource_id);

    ArrayList<Resource> getAllByCourseId(@Param("course_id") int course_id);
}




