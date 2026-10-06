package com.hdfc.netbanking.controller;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void testTransactionHistoryWhenUserNotLoggedIn() {

        when(session.getAttribute("userId"))
                .thenReturn(null);

        String result =
                transactionController.transactionHistory(
                        session,
                        model
                );

        assertEquals("redirect:/login", result);

        verify(session).getAttribute("userId");

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(model);
    }

    @Test
    void testTransactionHistoryWhenAccountNotFound() {

        Long userId = 1L;

        when(session.getAttribute("userId"))
                .thenReturn(userId);

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        String result =
                transactionController.transactionHistory(
                        session,
                        model
                );

        assertEquals("redirect:/dashboard", result);

        verify(accountRepository)
                .findByUserId(userId);

        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(model);
    }

    @Test
    void testTransactionHistorySuccessfully() {

        Long userId = 1L;

        Account account = new Account();

        account.setId(10L);
        account.setAccountNumber("501234567890");
        account.setBalance(20000.0);
        account.setAccountType("SAVINGS");

        Transaction transaction1 = new Transaction();

        transaction1.setId(1L);
        transaction1.setSenderAccount("501234567890");
        transaction1.setReceiverAccount("601234567890");
        transaction1.setAmount(5000.0);
        transaction1.setTransactionType("TRANSFER");
        transaction1.setStatus("SUCCESS");

        Transaction transaction2 = new Transaction();

        transaction2.setId(2L);
        transaction2.setSenderAccount("601234567890");
        transaction2.setReceiverAccount("501234567890");
        transaction2.setAmount(3000.0);
        transaction2.setTransactionType("TRANSFER");
        transaction2.setStatus("SUCCESS");

        List<Transaction> transactions =
                Arrays.asList(transaction1, transaction2);

        when(session.getAttribute("userId"))
                .thenReturn(userId);

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.of(account));

        when(transactionRepository
                .findBySenderAccountOrReceiverAccount(
                        "501234567890",
                        "501234567890"
                ))
                .thenReturn(transactions);

        String result =
                transactionController.transactionHistory(
                        session,
                        model
                );

        assertEquals("transactions", result);

        verify(accountRepository)
                .findByUserId(userId);

        verify(transactionRepository)
                .findBySenderAccountOrReceiverAccount(
                        "501234567890",
                        "501234567890"
                );

        verify(model)
                .addAttribute("account", account);

        verify(model)
                .addAttribute("transactions", transactions);
    }
}