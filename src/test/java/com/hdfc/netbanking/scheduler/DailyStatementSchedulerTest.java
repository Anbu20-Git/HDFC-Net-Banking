package com.hdfc.netbanking.scheduler;

import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;
import com.hdfc.netbanking.service.StatementPdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyStatementSchedulerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private StatementPdfService statementPdfService;

    @InjectMocks
    private DailyStatementScheduler dailyStatementScheduler;

    @Test
    void testGenerateDailyStatementSuccessfully() throws Exception {

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(5000.0);
        transaction1.setTransactionType("TRANSFER");
        transaction1.setStatus("SUCCESS");

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(3000.0);
        transaction2.setTransactionType("PAYMENT");
        transaction2.setStatus("SUCCESS");

        List<Transaction> transactions =
                Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(transactions);

        when(statementPdfService.generateDailyStatementPdf())
                .thenReturn(new byte[]{1, 2, 3});

        dailyStatementScheduler.generateDailyStatement();

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );

        verify(statementPdfService, times(1))
                .generateDailyStatementPdf();
    }

    @Test
    void testGenerateDailyStatementWithNoTransactions()
            throws Exception {

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());

        when(statementPdfService.generateDailyStatementPdf())
                .thenReturn(new byte[]{1, 2, 3});

        dailyStatementScheduler.generateDailyStatement();

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );

        verify(statementPdfService, times(1))
                .generateDailyStatementPdf();
    }

    @Test
    void testGenerateDailyStatementWhenPdfGenerationFails()
            throws Exception {

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());

        when(statementPdfService.generateDailyStatementPdf())
                .thenThrow(new RuntimeException("PDF generation failed"));

        assertDoesNotThrow(
                () -> dailyStatementScheduler.generateDailyStatement()
        );

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );

        verify(statementPdfService, times(1))
                .generateDailyStatementPdf();
    }
}