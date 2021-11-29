package com.hcsp.gsspring.mapper;

import com.hcsp.gsspring.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("insert into user (username, encryptedPassword, avatar, created_at, updated_at)\n" +
            "values (#{username}, #{encode}, '', now(), now())")
    void registerUser(@Param("username") String username, @Param("encode") String encode);

    @Select("SELECT id, username, encryptedPassword, avatar, created_at, updated_at FROM user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);
}
