package com.e_r_platform.config;

import com.e_r_platform.controller.Authorization.CustomAuthorizationManager;
import com.e_r_platform.controller.Authorization.JwtAuthenticationFilter;
import com.e_r_platform.controller.Authorization.JwtHandler;
import com.e_r_platform.model.User;
import com.e_r_platform.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.function.Supplier;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private CustomAuthorizationManager customAuthorizationManager;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/courses").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("admin")
//                        .requestMatchers(HttpMethod.POST, "/courses").hasAuthority("teacher")
//                        .requestMatchers(HttpMethod.PATCH, "/courses/{id}").hasAuthority("teacher")
//                        .requestMatchers(HttpMethod.DELETE, "/courses/{id}").hasAuthority("teacher")
//                        .requestMatchers(HttpMethod.POST, "/courses/{courseId}/resources").hasAuthority("teacher")
//                        .requestMatchers(HttpMethod.DELETE, "/courses/{courseId}/resources/{resourceId}").hasAuthority("teacher")
//                        .requestMatchers(HttpMethod.GET, "/users/{id}").access(customAuthorizationManager)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}