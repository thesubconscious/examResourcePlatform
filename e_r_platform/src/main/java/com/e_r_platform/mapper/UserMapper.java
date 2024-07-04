package com.e_r_platform.mapper;

import com.e_r_platform.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-04-27 15:42:34
* @Entity com.e_r_platform.model.User
*/

public interface UserMapper extends BaseMapper<User> {

    int create(@Param("email") String email, @Param("name") String name, @Param("password") String password, @Param(
            "identity") String identity);

    User login(@Param("email") String email, @Param("password") String password);

    User selectUserByName(@Param("name") String name);
    Optional<User> selectUserByEmail(@Param("email") String email);

    User selectUserByID(@Param("id") int user_id);

    int updateUser(@Param("user_id") int user_id, @Param("email") String email, @Param("name") String name,
                   @Param("password") String password,
                   @Param("identity") String identity);

    int deleteUserToken(@Param("token") String token);

}




