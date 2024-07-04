package com.e_r_platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.e_r_platform.mapper.UserMapper;
import com.e_r_platform.model.User;
import com.e_r_platform.service.impl.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<?> handleUserRegister(@RequestBody User user){
        int created = userService.register(user);
        if(created > 0){
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(created)
                    .toUri();
            logger.info("User registered successfully. Location: {}", location);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "User registered successfully.");
            responseBody.put("location", location.toString());
            return ResponseEntity.created(location).body(responseBody);
        }
        else if(created == -1){
            return ResponseEntity.badRequest().body("User already exist.");
        }
        else{
            return ResponseEntity.badRequest().body("Failed to register user.");
        }
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<?> handleUserLogin(@RequestBody User user, HttpServletResponse response) {
        String token = userService.login(user.getEmail(), user.getPassword());
        if (token != null) {
            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60); // 有效期为1小时
            response.addCookie(jwtCookie);
            return ResponseEntity.ok("Login success");
        } else {
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> handleUserLogOut(HttpServletResponse response){
        userService.logOut(response);
        return ResponseEntity.ok("Logout success");
    }


    @GetMapping ("/{id}")
    public ResponseEntity<?> handleGetUser(@PathVariable int id){
        User user = userService.getOne(id);
        if(user != null)
            return ResponseEntity.ok(user);
        else{
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> handleUserUpdate(@PathVariable int id, @RequestBody User user){
        user.setUser_id(id);
        int result = userService.update(user);
        if(result != 0){
            return ResponseEntity.ok("Update success");
        }
        else{
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }
}
