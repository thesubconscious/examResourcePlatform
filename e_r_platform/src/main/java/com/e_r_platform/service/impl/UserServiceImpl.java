package com.e_r_platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.e_r_platform.model.User;
import com.e_r_platform.service.UserService;
import com.e_r_platform.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-04-27 15:41:46
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    public int register(User user){
        User existedUser =  userMapper.selectUserByEmail(user.getEmail());
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
        User user = userMapper.login(email,password);
        if(user == null) return null;

        int token = user.hashCode();
        user.setToken(String.valueOf(token));
        userMapper.updateUserToken(user.getEmail(),user.getToken());
        return user.getToken();
    }

    public int logOut(String token){
        int result = userMapper.deleteUserToken(token);
        return result;
    }

    public User getOne(int user_id){
        User user = userMapper.selectUserByID(user_id);
        return user;
    }

    public int update(User user){
        int result = userMapper.updateUser(user.getUser_id(), user.getEmail(),user.getName(),user.getPassword(),
                user.getIdentity());
        return result;
    }
}




