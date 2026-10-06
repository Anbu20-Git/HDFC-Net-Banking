package com.hdfc.netbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.hdfc.netbanking.entity.User;
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
                any(User.class)
        );
    }

    @Test
    void registerUser_registersUserAndReturnsRegisterPage() {

        User user = new User();

        String result = registerController.registerUser(user, model);

        assertEquals("register", result);

        verify(userService).registerUser(user);

        verify(model).addAttribute(
                "message",
                "Registration successful!"
        );
    }
}