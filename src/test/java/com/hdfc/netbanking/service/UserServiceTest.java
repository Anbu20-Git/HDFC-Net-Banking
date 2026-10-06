package com.hdfc.netbanking.service;

import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser() {
        User user = new User(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210",
                "CUSTOMER"
        );

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals("USER", user.getRole());
        assertSame(user, result);

        verify(userRepository, times(1)).save(user);
    }
}