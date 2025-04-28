package com.e_r_platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.e_r_platform.controller.Authorization.JwtHandler;
import com.e_r_platform.mapper.UserMapper;
import com.e_r_platform.model.Course;
import com.e_r_platform.model.User;
import com.e_r_platform.service.impl.CourseStudentsServiceImpl;
import com.e_r_platform.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CourseStudentsServiceImpl courseStudentsService;
    @Autowired
    private JwtHandler jwtHandler;

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
            ResponseCookie cookie_jwt = userService.setCookie("JWT_TOKEN", token, true);
            response.addHeader(HttpHeaders.SET_COOKIE, cookie_jwt.toString());

            int userId = userService.getUserID(user.getEmail());
//            ResponseCookie cookie_uid = userService.setCookie("USER_ID", ""+userId);
//            response.addHeader(HttpHeaders.SET_COOKIE, cookie_uid.toString());

            Map<String, Object> body = new HashMap<>();
            body.put("message", "Login success");
            body.put("status", "success");
            body.put("userId", userId);
            body.put("token", token);//todo：找时间改了
            body.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(body);
        } else {
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> handleUserLogOut(HttpServletResponse response){
        ResponseCookie killJwt = userService.deleteCookie("JWT_TOKEN","/",true);
        response.addHeader(HttpHeaders.SET_COOKIE, killJwt.toString());
        return ResponseEntity.ok("Logout success");
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();

        String token = Arrays.stream(request.getCookies())
                .filter(c -> "JWT_TOKEN".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token != null && jwtHandler.validateToken(token)) {
            Claims claims = jwtHandler.parseClaims(token);

            // Token续期策略（剩余有效期小于5分钟时刷新）
            long remainingTime = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (remainingTime < 300_000) { // 5分钟阈值
                String newToken = jwtHandler.generateToken(jwtHandler.getUserDetails(token));
                response.setHeader(HttpHeaders.SET_COOKIE, userService.setCookie("JWT_TOKEN", newToken, true).toString());
            }

            result.put("isAuthenticated", true);
            result.put("expiresIn", remainingTime);
            return ResponseEntity.ok(result);
        }
        else {
            // 清理失效凭证
            response.setHeader(HttpHeaders.SET_COOKIE, userService.deleteCookie("JWT_TOKEN","/",true).toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
