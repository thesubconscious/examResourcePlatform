package com.e_r_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.e_r_platform.model.CourseStudents;
import com.e_r_platform.model.User;
import com.e_r_platform.service.CourseStudentsService;
import com.e_r_platform.mapper.CourseStudentsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【course_students】的数据库操作Service实现
* @createDate 2024-04-27 15:52:17
*/
@Service
public class CourseStudentsServiceImpl extends ServiceImpl<CourseStudentsMapper, CourseStudents>
    implements CourseStudentsService{

    @Autowired
    private CourseStudentsMapper courseStudentsMapper;

    public ArrayList<CourseStudents> getAll(int course_id){
        ArrayList<CourseStudents> list = courseStudentsMapper.getAllByCourseId(course_id);
        return list;
    }

    public int add(int course_id, int user_id){
        CourseStudents existedRecord = courseStudentsMapper.getOne(course_id,user_id);
        if(existedRecord != null) return -1;

        int result = courseStudentsMapper.addNewUserToCourse(course_id,user_id);

        return result;
    }

    public int delete(int course_id, int user_id){
        int result = courseStudentsMapper.deleteUser(course_id,user_id);

        return result;
    }
}




