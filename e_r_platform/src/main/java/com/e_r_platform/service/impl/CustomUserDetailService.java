package com.e_r_platform.service.impl;

import com.e_r_platform.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailService  implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        com.e_r_platform.model.User user = userMapper.selectUserByEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + identifier));

        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(user.getIdentity()));//有待修改。不应把身份直接作为权限。
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);

    }
}