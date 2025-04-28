package com.e_r_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.e_r_platform.config.WebConfig;
import com.e_r_platform.controller.UserController;
import com.e_r_platform.mapper.ResourceMapper;
import com.e_r_platform.model.Course;
import com.e_r_platform.service.CourseService;
import com.e_r_platform.mapper.CourseMapper;
import com.e_r_platform.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【course】的数据库操作Service实现
* @createDate 2024-04-27 15:52:13
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService{

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private WebConfig webConfig;

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    public ArrayList<Course> getAll(){
        ArrayList<Course> list = courseMapper.getAll();
//        logger.info(list.toString());
        System.out.println(list);
        return list;
    }

    public Course getOne(int course_id){
        Course course = courseMapper.getOne(course_id);
        return course;
    }

    public int create(Course course){
        Course existedCourse = courseMapper.searchByNameAndTeacherId(course.getName(),course.getTeacher_id());
        if(existedCourse!=null) return -1;

        int course_id = courseMapper.create(course);
        return course_id;
    }

    public int update(Course course, MultipartFile file) throws IOException {
        if (file!=null){
            //重命名文件
            String extension = file.getOriginalFilename() != null ?
                    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")) :
                    "";
            String newFileName = course.getCourse_id() + extension;
            String relativePath = "/course/" + newFileName;
            Path storagePath = Paths.get(webConfig.uploadBasePath, relativePath);
            course.setImg_path(relativePath);
            // 确保目录存在
            Files.createDirectories(storagePath.getParent());
            // 转存文件
            file.transferTo(storagePath);
        }

        int result = courseMapper.update(course);

        return result;
    }

    public int delete(int course_id){
        int result = courseMapper.deleteByCourse_id(course_id);
        return result;
    }
}




