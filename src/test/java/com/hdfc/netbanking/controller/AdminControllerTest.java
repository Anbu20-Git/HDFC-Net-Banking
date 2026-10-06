package com.hdfc.netbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.TransactionRepository;
import com.hdfc.netbanking.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

class AdminControllerTest {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    private AdminController adminController;

    private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);

        adminController = new AdminController(
                userRepository,
                accountRepository,
                transactionRepository
        );

        session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    @Test
    void adminDashboard_whenNotLoggedIn_redirectsToLogin() {

        when(session.getAttribute("userId")).thenReturn(null);

        String result = adminController.adminDashboard(session, model);

        assertEquals("redirect:/login", result);

        verify(userRepository, never()).findAll();
        verify(accountRepository, never()).findAll();
        verify(transactionRepository, never()).findAll();
    }

    @Test
    void adminDashboard_whenNotAdmin_redirectsToDashboard() {

        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("userRole")).thenReturn("USER");

        String result = adminController.adminDashboard(session, model);

        assertEquals("redirect:/dashboard", result);

        verify(userRepository, never()).findAll();
        verify(accountRepository, never()).findAll();
        verify(transactionRepository, never()).findAll();
    }

    @Test
    void adminDashboard_whenAdmin_loadsDataAndReturnsAdminDashboard() {

        when(session.getAttribute("userId")).thenReturn(1L);
        when(session.getAttribute("userRole")).thenReturn("ADMIN");

        List<User> users = List.of(new User());
        List<Account> accounts = List.of(new Account());
        List<Transaction> transactions = List.of(new Transaction());

        when(userRepository.findAll()).thenReturn(users);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(transactionRepository.findAll()).thenReturn(transactions);

        String result = adminController.adminDashboard(session, model);

        assertEquals("admin-dashboard", result);

        verify(userRepository).findAll();
        verify(accountRepository).findAll();
        verify(transactionRepository).findAll();

        verify(model).addAttribute("users", users);
        verify(model).addAttribute("accounts", accounts);
        verify(model).addAttribute("transactions", transactions);
    }
}