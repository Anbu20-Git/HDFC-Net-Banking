package com.hdfc.netbanking.service;

import com.hdfc.netbanking.dto.RegisterRequest;
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

        RegisterRequest request = new RegisterRequest(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210"
        );

        User savedUser = new User(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210",
                "USER"
        );

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(request);

        assertEquals("USER", result.getRole());
        assertEquals("Anbu", result.getName());
        assertEquals("anbu@gmail.com", result.getEmail());
        assertEquals("9876543210", result.getPhone());

        verify(userRepository, times(1)).save(any(User.class));
    }
}