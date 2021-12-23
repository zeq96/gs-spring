package com.hcsp.gsspring.service;

import com.hcsp.gsspring.dao.UserDao;
import com.hcsp.gsspring.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Inject
    public UserService(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void registerUser(String username, String encode) {
        userDao.registerUser(username, bCryptPasswordEncoder.encode(encode));
    }

    public User getUserByName(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User currentUser = getUserByName(username);
        if (currentUser == null) {
            throw new UsernameNotFoundException("用户名不存在: " + username);
        }
        String encryptedPassword = currentUser.getEncryptedPassword();
        return new org.springframework.security.core.userdetails.User(username, encryptedPassword, Collections.emptyList());
    }
}
