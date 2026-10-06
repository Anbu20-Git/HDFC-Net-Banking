package com.hdfc.netbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.hdfc.netbanking.dto.RegisterRequest;
import com.hdfc.netbanking.service.UserService;

class RegisterControllerTest {

    private UserService userService;

    private RegisterController registerController;

    private Model model;

    @BeforeEach
    void setUp() {

        userService = mock(UserService.class);

        registerController = new RegisterController(userService);

        model = mock(Model.class);

    }

    @Test
    void showRegisterPage_returnsRegisterPage() {

        String result = registerController.showRegisterPage(model);

        assertEquals("register", result);

        verify(model).addAttribute(
                eq("user"),
                any(RegisterRequest.class)
        );

    }

    @Test
    void registerUser_registersUserAndReturnsRegisterPage() {

        RegisterRequest request = new RegisterRequest(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210"
        );

        String result = registerController.registerUser(request, model);

        assertEquals("register", result);

        verify(userService).registerUser(request);

        verify(model).addAttribute(
                "message",
                "Registration successful!"
        );

    }

}