package com.e_r_platform.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.e_r_platform")
@MapperScan("com.e_r_platform.mapper")
public class application {

    public static void main(String[] args) {
        SpringApplication.run(application.class, args);
    }
}
