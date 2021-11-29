package com.hcsp.gsspring.controller;

import com.hcsp.gsspring.entity.AuthResponse;
import com.hcsp.gsspring.entity.User;
import com.hcsp.gsspring.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class AuthController {

    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public AuthResponse registerUser(@RequestBody Map<String, String> unameAndPassword) {
        String username = unameAndPassword.get("username");
        String password = unameAndPassword.get("password");

        if (username.length() < 1 || username.length() > 15) {
            return AuthResponse.failure("Illegal username");
        }
        if (password.length() < 6 || password.length() > 15) {
            return AuthResponse.failure("Illegal password");
        }
        try {
            userService.registerUser(username, password);
            return AuthResponse.success("注册成功", null);
        } catch (DuplicateKeyException e) {
            return AuthResponse.failure("用户名已存在");
        }

    }

    @PostMapping("/auth/login")
    @ResponseBody
    public AuthResponse login(@RequestBody Map<String, String> unameAndPassword) {
        String username = unameAndPassword.get("username");
        String password = unameAndPassword.get("password");

        try {
            // 获取用户token
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            // 鉴权
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);

            return AuthResponse.success("登录成功", userService.getUserByName(username));
        } catch (BadCredentialsException e) {
            return AuthResponse.failure("密码不正确");
        } catch (UsernameNotFoundException e) {
            return AuthResponse.failure("用户不存在");
        }

    }

    @GetMapping("/auth")
    @ResponseBody
    public AuthResponse isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByName(authentication == null ? null : authentication.getName());
        if (currentUser == null) {
            return AuthResponse.failure("用户没有登录");
        }
        return AuthResponse.success("用户登录成功", currentUser);
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public AuthResponse logOut() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getUserByName(currentUsername);
        if (currentUser == null) {
            return AuthResponse.failure("用户尚未登录");
        }
        SecurityContextHolder.clearContext();
        return AuthResponse.success("用户注销成功", null);
    }

}
