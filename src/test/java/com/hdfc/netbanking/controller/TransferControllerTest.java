package com.hdfc.netbanking.controller;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.UserRepository;
import com.hdfc.netbanking.service.TransferService;
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
class TransferControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferService transferService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private TransferController transferController;

    @Test
    void testTransferPageWhenUserNotLoggedIn() {

        when(session.getAttribute("userId"))
                .thenReturn(null);

        String result =
                transferController.transferPage(session, model);

        assertEquals("redirect:/login", result);

        verify(session).getAttribute("userId");
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(model);
    }

    @Test
    void testTransferPageWhenUserLoggedIn() {

        Long userId = 1L;

        User user = new User(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210",
                "USER"
        );

        Account account = new Account();
        account.setId(10L);
        account.setAccountNumber("501234567890");
        account.setBalance(20000.0);
        account.setAccountType("SAVINGS");
        account.setUser(user);

        when(session.getAttribute("userId"))
                .thenReturn(userId);

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.of(account));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        String result =
                transferController.transferPage(session, model);

        assertEquals("transfer", result);

        verify(accountRepository)
                .findByUserId(userId);

        verify(userRepository)
                .findById(userId);

        verify(model)
                .addAttribute("user", user);

        verify(model)
                .addAttribute("account", account);
    }

    @Test
    void testTransferWhenUserNotLoggedIn() {

        when(session.getAttribute("userId"))
                .thenReturn(null);

        String result =
                transferController.transfer(
                        "601234567890",
                        5000.0,
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verify(session).getAttribute("userId");
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(transferService);
        verifyNoInteractions(model);
    }

    @Test
    void testTransferWhenSenderAccountNotFound() {

        Long userId = 1L;

        when(session.getAttribute("userId"))
                .thenReturn(userId);

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        String result =
                transferController.transfer(
                        "601234567890",
                        5000.0,
                        session,
                        model
                );

        assertEquals("redirect:/dashboard", result);

        verify(accountRepository)
                .findByUserId(userId);

        verifyNoInteractions(transferService);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(model);
    }

    @Test
    void testSuccessfulTransfer() {

        Long userId = 1L;

        User user = new User(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210",
                "USER"
        );

        Account sender = new Account();
        sender.setId(10L);
        sender.setAccountNumber("501234567890");
        sender.setBalance(20000.0);
        sender.setAccountType("SAVINGS");
        sender.setUser(user);

        Account updatedAccount = new Account();
        updatedAccount.setId(10L);
        updatedAccount.setAccountNumber("501234567890");
        updatedAccount.setBalance(15000.0);
        updatedAccount.setAccountType("SAVINGS");
        updatedAccount.setUser(user);

        when(session.getAttribute("userId"))
                .thenReturn(userId);

        when(accountRepository.findByUserId(userId))
                .thenReturn(
                        Optional.of(sender),
                        Optional.of(updatedAccount)
                );

        when(transferService.transferMoney(
                "501234567890",
                "601234567890",
                5000.0
        )).thenReturn("Transfer successful");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        String result =
                transferController.transfer(
                        "601234567890",
                        5000.0,
                        session,
                        model
                );

        assertEquals("transfer", result);

        verify(transferService)
                .transferMoney(
                        "501234567890",
                        "601234567890",
                        5000.0
                );

        verify(accountRepository, times(2))
                .findByUserId(userId);

        verify(userRepository)
                .findById(userId);

        verify(model)
                .addAttribute("user", user);

        verify(model)
                .addAttribute("account", updatedAccount);

        verify(model)
                .addAttribute(
                        "message",
                        "Transfer successful"
                );
    }
}