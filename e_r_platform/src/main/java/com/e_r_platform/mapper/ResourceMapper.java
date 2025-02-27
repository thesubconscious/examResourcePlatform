package com.e_r_platform.mapper;

import com.e_r_platform.model.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【resource】的数据库操作Mapper
* @createDate 2024-04-27 15:52:20
* @Entity com.e_r_platform.model.Resource
*/
public interface ResourceMapper extends BaseMapper<Resource> {
    List<Resource> selectChildren(@Param("parentId") Integer parentId);
    List<Resource> selectEntireCourseTree(Integer courseId);

    void shiftOrdersForInsert(@Param("courseId") Integer courseId,
                              @Param("parentId") Integer parentId,
                              @Param("order") Integer order);

    int deleteSubtree(Integer nodeId);

    int smartUpdateNodePosition(@Param("entity") Resource entity);

    int swapDisplayOrder(@Param("id1") Integer id1, @Param("id2") Integer id2);
}




