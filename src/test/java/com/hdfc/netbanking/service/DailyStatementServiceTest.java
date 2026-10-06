package com.hdfc.netbanking.service;

import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyStatementServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DailyStatementService dailyStatementService;

    @Test
    void testGetTodayTransactions() {

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(5000.0);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(3000.0);

        List<Transaction> transactions =
                Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(transactions);

        List<Transaction> result =
                dailyStatementService.getTodayTransactions();

        assertEquals(2, result.size());
        assertSame(transaction1, result.get(0));
        assertSame(transaction2, result.get(1));

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );
    }

    @Test
    void testGetTodayTransactionsWhenNoTransactions() {

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());

        List<Transaction> result =
                dailyStatementService.getTodayTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );
    }

    @Test
    void testGetTotalAmount() {

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(5000.0);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(3000.0);

        Transaction transaction3 = new Transaction();
        transaction3.setAmount(2000.0);

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(
                Arrays.asList(
                        transaction1,
                        transaction2,
                        transaction3
                )
        );

        double total = dailyStatementService.getTotalAmount();

        assertEquals(10000.0, total);

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );
    }

    @Test
    void testGetTotalAmountWhenNoTransactions() {

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());

        double total = dailyStatementService.getTotalAmount();

        assertEquals(0.0, total);
    }

    @Test
    void testGetTotalTransactions() {

        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(Arrays.asList(transaction1, transaction2));

        int total = dailyStatementService.getTotalTransactions();

        assertEquals(2, total);
    }

    @Test
    void testGetTotalTransactionsWhenNoTransactions() {

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());

        int total = dailyStatementService.getTotalTransactions();

        assertEquals(0, total);
    }
}