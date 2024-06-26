package com.e_r_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.e_r_platform.model.Resource;
import com.e_r_platform.model.ResourceCompletions;
import com.e_r_platform.service.ResourceCompletionsService;
import com.e_r_platform.mapper.ResourceCompletionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【resource_completions】的数据库操作Service实现
* @createDate 2024-04-27 15:52:23
*/
@Service
public class ResourceCompletionsServiceImpl extends ServiceImpl<ResourceCompletionsMapper, ResourceCompletions>
    implements ResourceCompletionsService{
    @Autowired
    private ResourceCompletionsMapper resourceCompletionsMapper;

    public int addNewCompletion(ResourceCompletions resourceCompletions){
        ResourceCompletions existed = resourceCompletionsMapper.getOneByResourceIDAndStudentID(resourceCompletions.getResource_id(),
                resourceCompletions.getStudent_id());
        if(existed != null) return -1;

        int result = resourceCompletionsMapper.insertOne(resourceCompletions.getResource_id(),
                resourceCompletions.getStudent_id());
        return result;
    }

    public ArrayList<Resource> getResourceList(int course_id, int student_id){
        ArrayList<Resource> list = resourceCompletionsMapper.getResourcesByCourseIDAndStudentID(course_id,student_id);

        return list;
    }
}




