package com.e_r_platform.controller;

import com.e_r_platform.model.Resource;
import com.e_r_platform.model.ResourceCompletions;
import com.e_r_platform.service.impl.ResourceCompletionsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/courses/{course_id}")
public class ResourceCompletionController {
    @Autowired
    private ResourceCompletionsServiceImpl resourceCompletionsService;

    @PostMapping("/resources/{resource_id}/completion")
    public ResponseEntity<?> handleAddNewCompletion(@PathVariable int resource_id, @RequestBody int student_id){
        ResourceCompletions resourceCompletions = new ResourceCompletions();
        resourceCompletions.setResource_id(resource_id);
        resourceCompletions.setStudent_id(student_id);
        int result = resourceCompletionsService.addNewCompletion(resourceCompletions);

        if(result > 0){
            return ResponseEntity.ok("Completion successfully");
        }
        else if (result == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The resource is already completion");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot completion");
        }
    }

    @GetMapping("/students/{student_id}")
    public ResponseEntity<?> handleGetResourceListStudentInCourse(@PathVariable int course_id,
                                                                  @PathVariable int student_id){
        ArrayList<Resource> list = resourceCompletionsService.getResourceList(course_id,student_id);
        if(!list.isEmpty()){
            return ResponseEntity.ok(list);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no completion resource list");
        }
    }
}
