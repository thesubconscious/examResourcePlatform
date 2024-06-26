package com.e_r_platform.controller;

import com.e_r_platform.model.Resource;
import com.e_r_platform.service.impl.ResourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/courses/{id}/resources")
public class ResourceController {
    @Autowired
    private ResourceServiceImpl resourceService;

    @PostMapping
    public ResponseEntity<?> handleFileUpload(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        try {
            Resource resource = resourceService.uploadFile(id, file);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Resource upload successfully.");
            responseBody.put("resource_id", resource.getResource_id());
            return ResponseEntity.ok(responseBody);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Resource upload failed: " + e.getMessage());
        }
    }

    private MediaType contentType(Path filePath) {
        try {
            // Content type determination
            String contentType = Files.probeContentType(filePath);
            return MediaType.parseMediaType(contentType);
        } catch (IOException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @GetMapping("/{resource_id}")
    public ResponseEntity<?> handleFileDownload(@PathVariable int resource_id) {
        try {
            File file = resourceService.downloadFile(resource_id);
            Path filePath = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));
            String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .contentType(contentType(filePath))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> handleGetAllResourcesByCouseID(@PathVariable int id){
        ArrayList<Resource> list = resourceService.getAllResourcesByCourseID(id);
        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no resource");
        }
        else{
            return ResponseEntity.ok(list);
        }
    }

    @DeleteMapping("/{resource_id}")
    public ResponseEntity<?> handleFileDelete(@PathVariable int resource_id) {
        try {
            if(resourceService.deleteFile(resource_id)){
                return ResponseEntity.ok("Resource deleted successfully");
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete resource");
            }
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
