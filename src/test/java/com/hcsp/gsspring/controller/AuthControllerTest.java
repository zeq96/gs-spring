package com.hcsp.gsspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcsp.gsspring.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService mockService;
    @Mock
    private AuthenticationManager mockManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(mockService, mockManager))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
    }

    @Test
    void returnNotLoginByDeafult() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth"))
                .andExpect(status().isOk())
                .andExpect(result ->
                        Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录")));
    }

    @Test
    void testLogin() throws Exception {

        // 第一次未登录, /auth 接口返回未登录状态
        mockMvc.perform(MockMvcRequestBuilders.get("/auth"))
                .andExpect(status().isOk())
                .andExpect(result ->
                        Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录")));

        // 模拟登录的用户名密码
        Map<String, String> usernameAndPassword = new HashMap<>();
        usernameAndPassword.put("username", "testUser");
        usernameAndPassword.put("password", "password");

        Mockito.when(mockService.loadUserByUsername("testUser"))
                .thenReturn(new User("testUser", passwordEncoder.encode("password"), Collections.emptyList()));
        Mockito.when(mockService.getUserByName("testUser"))
                .thenReturn(new com.hcsp.gsspring.entity.User(1, "testUser", passwordEncoder.encode("password")));

        // 模拟登录操作, 获取登录后的用户session
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(usernameAndPassword)))
                .andExpect(status().isOk())
                .andExpect(result ->
                        Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录成功")))
                .andReturn();
        HttpSession session = response.getRequest().getSession();

        // 再次请求 /auth 接口, 响应中应包含当前用户名
        mockMvc.perform(MockMvcRequestBuilders.get("/auth").session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("testUser"));
                });
    }

}