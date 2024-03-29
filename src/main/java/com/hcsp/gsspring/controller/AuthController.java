package com.hcsp.gsspring.controller;

import com.hcsp.gsspring.entity.AuthResponse;
import com.hcsp.gsspring.service.AuthService;
import com.hcsp.gsspring.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager, AuthService authService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
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
        } catch (DuplicateKeyException e) {
            return AuthResponse.failure("用户名已存在");
        }
        login(unameAndPassword);
        return AuthResponse.success("注册成功", userService.getUserByName(username));
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
        return authService.getCurrentUser()
                .map(user -> AuthResponse.success("用户登录成功", user))
                .orElse(AuthResponse.failure("用户没有登录"));
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public AuthResponse logout() {
        AuthResponse response = authService.getCurrentUser()
                .map(user -> AuthResponse.success("注销成功", user))
                .orElse(AuthResponse.failure("用户尚未登录"));
        SecurityContextHolder.clearContext();
        return response;
    }

}
