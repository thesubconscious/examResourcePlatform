package com.e_r_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.e_r_platform.model.Resource;
import com.e_r_platform.service.ResourceService;
import com.e_r_platform.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
* @author Administrator
* @description 针对表【resource】的数据库操作Service实现
* @createDate 2024-04-27 15:52:20
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{
    @Autowired
    private ResourceMapper resourceMapper;

    public int add(Resource resource){
        int resource_id = resourceMapper.insertOne(resource.getCourse_id(),resource.getName(),resource.getPath());

        return resource_id;
    }

    public Resource getResourceInfo(int resource_id){
        Resource resource = resourceMapper.searchAllByResourceId(resource_id);

        return resource;
    }

    public Resource uploadFile(int course_id, int node_id, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        String uploadDir = "C:/uploads/course" + course_id + "/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        File destFile = new File(uploadDir + fileName);
        file.transferTo(destFile);

        Resource resource = new Resource();
        resource.setCourse_id(course_id);
        resource.setName(fileName);
        resource.setPath(uploadDir);

        int resourceId = add(resource);
        resource.setResource_id(resourceId);

        return resource;
    }

    public File downloadFile(int resource_id) throws IOException {
        Resource resource = getResourceInfo(resource_id);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found for id: " + resource_id);
        }

        String filePath = resource.getPath() + resource.getName();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found at path: " + filePath);
        }
        return file;
    }

    public boolean deleteFile(int resource_id) throws IOException {
        Resource resource = getResourceInfo(resource_id);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found for id: " + resource_id);
        }

        String filePath = resource.getPath() + resource.getName();
        File file = new File(filePath);
        if (file.exists()) {
            resourceMapper.deleteByResourceId(resource_id);
            if (file.delete()) {
                return true;
            } else {
                throw new IOException("Failed to delete the file: " + filePath);
            }
        } else {
            throw new FileNotFoundException("File not found at path: " + filePath);
        }
    }

    public ArrayList<Resource> getAllResourcesByCourseID(int course_id){
        ArrayList<Resource> list = resourceMapper.getAllByCourseId(course_id);

        return list;
    }
}




