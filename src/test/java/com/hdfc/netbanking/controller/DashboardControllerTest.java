package com.hdfc.netbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

class DashboardControllerTest {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private DashboardController dashboardController;

    private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);

        dashboardController =
                new DashboardController(userRepository, accountRepository);

        session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    @Test
    void dashboard_whenNotLoggedIn_redirectsToLogin() {

        when(session.getAttribute("userId")).thenReturn(null);

        String result = dashboardController.dashboard(session, model);

        assertEquals("redirect:/login", result);

        verify(userRepository, never()).findById(anyLong());
        verify(accountRepository, never()).findByUserId(anyLong());
    }

    @Test
    void dashboard_whenAdmin_redirectsToAdminDashboard() {

        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("userRole")).thenReturn("ADMIN");

        String result = dashboardController.dashboard(session, model);

        assertEquals("redirect:/admin/dashboard", result);

        verify(userRepository, never()).findById(anyLong());
        verify(accountRepository, never()).findByUserId(anyLong());
    }

    @Test
    void dashboard_whenUserNotFound_invalidatesSessionAndRedirectsToLogin() {

        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("userRole")).thenReturn("USER");

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        String result = dashboardController.dashboard(session, model);

        assertEquals("redirect:/login", result);

        verify(session).invalidate();
        verify(accountRepository, never()).findByUserId(anyLong());
    }

    @Test
    void dashboard_whenValidUser_returnsDashboard() {

        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("userRole")).thenReturn("USER");

        User user = new User();
        user.setId(1L);
        user.setName("Anbu");

        Account account = new Account();
        account.setAccountNumber("501234567890");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(account));

        String result = dashboardController.dashboard(session, model);

        assertEquals("dashboard", result);

        verify(userRepository).findById(1L);
        verify(accountRepository).findByUserId(1L);

        verify(model).addAttribute("user", user);
        verify(model).addAttribute("account", account);
    }
}