package com.hcsp.gsspring.dao;

import com.hcsp.gsspring.entity.User;
import com.hcsp.gsspring.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

@Repository
public class UserDao {

    private UserMapper userMapper;

    @Inject
    public UserDao(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void registerUser(String username, String encode) {
        userMapper.registerUser(username, encode);
    }

    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }



}
