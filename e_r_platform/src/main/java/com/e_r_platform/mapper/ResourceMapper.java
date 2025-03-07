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
    int insertResource(Resource resource);
    int updateResource(Resource resource);
    int decrementOrderAfterDelete(@Param("parentId") Integer parentId,
                                  @Param("deletedOrder") int deletedOrder);
    int deleteById(@Param("nodeId") int nodeId);
    int selectMaxOrderUnderParent(@Param("parentId") int parentId);
    Resource selectById(@Param("nodeId") int nodeId);
    List<Resource> selectChildren(@Param("parentId") Integer parentId);
}




