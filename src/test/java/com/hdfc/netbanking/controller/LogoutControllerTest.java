package com.hdfc.netbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpSession;

class LogoutControllerTest {

    private LogoutController logoutController;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        logoutController = new LogoutController();
        session = mock(HttpSession.class);
    }

    @Test
    void logout_invalidatesSessionAndRedirectsToLogin() {

        String result = logoutController.logout(session);

        assertEquals("redirect:/login", result);

        verify(session).invalidate();
    }
}