package com.e_r_platform.controller;

import com.e_r_platform.model.User;
import com.e_r_platform.service.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setEmail("460356823@qq.com");
        user.setName("Xiao");
        user.setPassword("123456");
        user.setIdentity("admin");

        int created = userService.register(user);//懒得处理了
    }
}