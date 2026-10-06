package com.hdfc.netbanking.service;

import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementPdfServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private StatementPdfService statementPdfService;

    @Test
    void testGenerateDailyStatementPdfWithTransactions() throws Exception {

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setSenderAccount("501234567890");
        transaction1.setReceiverAccount("601234567890");
        transaction1.setAmount(5000.0);
        transaction1.setTransactionType("TRANSFER");
        transaction1.setStatus("SUCCESS");
        transaction1.setTransactionDate(LocalDateTime.now());

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setSenderAccount("601234567890");
        transaction2.setReceiverAccount("701234567890");
        transaction2.setAmount(3000.0);
        transaction2.setTransactionType("PAYMENT");
        transaction2.setStatus("SUCCESS");
        transaction2.setTransactionDate(LocalDateTime.now());

        List<Transaction> transactions =
                Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(transactions);

        byte[] pdf =
                statementPdfService.generateDailyStatementPdf();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);

        // A valid PDF starts with "%PDF"
        assertEquals('%', (char) pdf[0]);
        assertEquals('P', (char) pdf[1]);
        assertEquals('D', (char) pdf[2]);
        assertEquals('F', (char) pdf[3]);

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );
    }

    @Test
    void testGenerateDailyStatementPdfWithNoTransactions() throws Exception {

        when(transactionRepository.findByTransactionDateBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());

        byte[] pdf =
                statementPdfService.generateDailyStatementPdf();

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);

        // A valid PDF starts with "%PDF"
        assertEquals('%', (char) pdf[0]);
        assertEquals('P', (char) pdf[1]);
        assertEquals('D', (char) pdf[2]);
        assertEquals('F', (char) pdf[3]);

        verify(transactionRepository, times(1))
                .findByTransactionDateBetween(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );
    }
}