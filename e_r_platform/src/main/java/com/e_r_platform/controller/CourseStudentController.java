package com.e_r_platform.controller;

import com.e_r_platform.model.CourseStudents;
import com.e_r_platform.service.impl.CourseStudentsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/courses/{id}/students")
public class CourseStudentController {
    @Autowired
    private CourseStudentsServiceImpl courseStudentsService;

    @GetMapping
    public ResponseEntity<?> handleGetAllUser(@PathVariable int id){
        ArrayList<CourseStudents> list = courseStudentsService.getAllStudents(id);

        if(list!=null)
            return ResponseEntity.ok(list);
        else
            return ResponseEntity.badRequest().body("No student is found");
    }

    @PostMapping
    public ResponseEntity<?> handleAddNewStudentToCourse(@PathVariable int id, @RequestBody int user_id){
        int result = courseStudentsService.add(id, user_id);

        if(result > 0){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(result)
                    .toUri();

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Student added successfully.");
            responseBody.put("location", location.toString());
            return ResponseEntity.created(location).body(responseBody);
        }
        else if(result == -1){
            return ResponseEntity.badRequest().body("Student already exist.");
        }
        else{
            return ResponseEntity.badRequest().body("Failed to add Student.");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> handleDeleteUserFromCourse(@PathVariable int id, @RequestBody int user_id){
        int result = courseStudentsService.add(id, user_id);

        if (result != 0){
            return ResponseEntity.ok("Delete Successfully.");
        }
        else {
            return ResponseEntity.badRequest().body("Delete failed.");
        }
    }
}
