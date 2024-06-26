package com.e_r_platform.controller;

import com.e_r_platform.model.Course;
import com.e_r_platform.service.impl.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseServiceImpl courseService;

    @GetMapping
    public ResponseEntity<?> handleGetAllCourse(){
        ArrayList<Course> list = courseService.getAll();
        if(list!=null)
            return ResponseEntity.ok(list);
        else
            return ResponseEntity.badRequest().body("There is no course.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> handleGetCourse(@PathVariable int id){
        Course course = courseService.getOne(id);
        if(course!=null)
            return ResponseEntity.ok(course);
        else
            return ResponseEntity.badRequest().body("There is no course.");
    }

    @PostMapping
    public ResponseEntity<?> handleCreateCourse(@RequestBody Course course){
        int course_id = courseService.create(course);
        if(course_id > 0){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(course_id)
                    .toUri();

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Course create successfully.");
            responseBody.put("location", location.toString());
            return ResponseEntity.created(location).body(responseBody);
        }
        else if(course_id == -1){
            return ResponseEntity.badRequest().body("Course already exist.");
        }
        else{
            return ResponseEntity.badRequest().body("Failed to create course.");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> handleUpdateCourse(@RequestBody Course course, @PathVariable int id){
        course.setCourse_id(id);
        int result = courseService.update(course);

        if(result!=0)
            return ResponseEntity.ok("Update success");
        else
            return ResponseEntity.badRequest().body("Course does not exist");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable int id){
        int result = courseService.delete(id);

        if(result!=0)
            return ResponseEntity.ok("Delete success");
        else
            return ResponseEntity.badRequest().body("Course does not exist");
    }

}
