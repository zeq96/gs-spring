package com.hcsp.gsspring.service;

import com.hcsp.gsspring.dao.UserDao;
import com.hcsp.gsspring.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDao mockDao;
    @Mock
    private BCryptPasswordEncoder mockEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser() {
        // given:
        when(mockEncoder.encode("testPass")).thenReturn("encodedPass");
        // when:
        userService.registerUser("testUser", "testPass");
        // then:
        verify(mockDao).registerUser("testUser", "encodedPass");
    }

    @Test
    void testGetUserByName() {
        userService.getUserByName("testUser");
        verify(mockDao).findUserByUsername("testUser");
    }

    @Test
    void testThrowExceptionWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("testUser"));

    }

    @Test
    void testReturnUserDetailsWhenUserFound() {
        when(mockDao.findUserByUsername("testUser"))
                .thenReturn(new User(1, "testUser", "",
                        Instant.now(), Instant.now(), "encodedPass"));

        UserDetails testUser = userService.loadUserByUsername("testUser");
        Assertions.assertEquals("testUser", testUser.getUsername());
        Assertions.assertEquals("encodedPass", testUser.getPassword());
    }
}
