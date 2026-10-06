package com.hdfc.netbanking.controller;

import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private LoginController loginController;

    @Test
    void testHome() {

        String result = loginController.home();

        assertEquals("login", result);
    }

    @Test
    void testLoginPage() {

        String result = loginController.loginPage();

        assertEquals("login", result);
    }

    @Test
    void testLoginWithInvalidEmail() {

        when(userRepository.findByEmail("wrong@gmail.com"))
                .thenReturn(Optional.empty());

        String result = loginController.login(
                "wrong@gmail.com",
                "wrongpassword",
                session,
                model
        );

        assertEquals("login", result);

        verify(userRepository)
                .findByEmail("wrong@gmail.com");

        verify(model)
                .addAttribute(
                        "error",
                        "Invalid email or password"
                );

        verifyNoInteractions(session);
    }

    @Test
    void testLoginWithInvalidPassword() {

        User user = new User(
                "Anbu",
                "anbu@gmail.com",
                "correctPassword",
                "9876543210",
                "USER"
        );

        user.setId(1L);

        when(userRepository.findByEmail("anbu@gmail.com"))
                .thenReturn(Optional.of(user));

        String result = loginController.login(
                "anbu@gmail.com",
                "wrongPassword",
                session,
                model
        );

        assertEquals("login", result);

        verify(model)
                .addAttribute(
                        "error",
                        "Invalid email or password"
                );

        verifyNoInteractions(session);
    }

    @Test
    void testLoginAsNormalUser() {

        User user = new User(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210",
                "USER"
        );

        user.setId(1L);

        when(userRepository.findByEmail("anbu@gmail.com"))
                .thenReturn(Optional.of(user));

        String result = loginController.login(
                "anbu@gmail.com",
                "password123",
                session,
                model
        );

        assertEquals("redirect:/dashboard", result);

        verify(session)
                .setAttribute("userId", 1L);

        verify(session)
                .setAttribute("userRole", "USER");

        verify(session)
                .setAttribute("userName", "Anbu");

        verifyNoInteractions(model);
    }

    @Test
    void testLoginAsAdmin() {

        User user = new User(
                "HDFC Admin",
                "admin@hdfc.com",
                "admin123",
                "9999999999",
                "ADMIN"
        );

        user.setId(99L);

        when(userRepository.findByEmail("admin@hdfc.com"))
                .thenReturn(Optional.of(user));

        String result = loginController.login(
                "admin@hdfc.com",
                "admin123",
                session,
                model
        );

        assertEquals(
                "redirect:/admin/dashboard",
                result
        );

        verify(session)
                .setAttribute("userId", 99L);

        verify(session)
                .setAttribute("userRole", "ADMIN");

        verify(session)
                .setAttribute("userName", "HDFC Admin");

        verifyNoInteractions(model);
    }
}