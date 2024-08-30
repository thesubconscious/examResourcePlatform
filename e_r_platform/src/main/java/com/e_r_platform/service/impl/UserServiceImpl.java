package com.e_r_platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.e_r_platform.controller.Authorization.JwtHandler;
import com.e_r_platform.model.User;
import com.e_r_platform.service.UserService;
import com.e_r_platform.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-04-27 15:41:46
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtHandler jwtHandler;
    @Autowired
    private CustomUserDetailService userDetailService;

    User checkUserStorage(User user){
        String cacheKey = "user:" + user.getUser_id();
        User existedUser = (User)redisTemplate.opsForValue().get(cacheKey);
        if (existedUser == null) {
            Optional<User> optionalUser = userMapper.selectUserByEmail(user.getEmail());
            if (optionalUser.isPresent()) {
                existedUser = optionalUser.get();
                redisTemplate.opsForValue().set(cacheKey, existedUser); // 重新缓存数据
            }
        }
        return existedUser;
    }

    public ResponseCookie setCookie(String name, String value, Boolean httpOnly, String path, int maxAge) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(maxAge)
                .httpOnly(httpOnly)
                .sameSite("None")
//                .secure(true)
                .build();
    }
    public ResponseCookie setCookie(String name, String value) {
        return setCookie(name, value, false,"/", 60 * 60 * 24);
    }
    public ResponseCookie setCookie(String name, String value, Boolean httpOnly) {
        return setCookie(name, value, httpOnly,"/", 60 * 60 * 24);
    }

    public ResponseCookie deleteCookie(String name, String path, Boolean httpOnly) {
        return ResponseCookie.from(name, "")
                .path(path)
                .maxAge(0) // 设置maxAge为0表示删除Cookie
                .httpOnly(httpOnly)
                .sameSite("None")
//                .secure(true)
                .build();
    }
    public ResponseCookie deleteCookie(String name) {
        return deleteCookie(name,"/",false);
    }

    public int register(User user){
        User existedUser =  checkUserStorage(user);
        if(existedUser!=null){
            return -1;
        }

        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();
        String identity = user.getIdentity();
        return userMapper.create(email, name,password,identity);
    }

    public String login(String email, String password) {
        User user = userMapper.login(email, password);
        if (user != null) {
            return jwtHandler.generateToken(userDetailService.loadUserByUsername(email));
        }
        return null;
    }

    public void logOut(HttpServletResponse response){
        deleteCookie("JWT_TOKEN");
//        deleteCookie("USER_ID");
    }

    public User getOne(int user_id){
        User user = userMapper.selectUserByID(user_id);
        return user;
    }

    public int getUserID(String email){
        Optional<User> user = userMapper.selectUserByEmail(email);
        return user.get().getUser_id();
    }

    public int update(User user){
        int result = userMapper.updateUser(user.getUser_id(), user.getEmail(),user.getName(),user.getPassword(),
                user.getIdentity());
        return result;
    }
}




