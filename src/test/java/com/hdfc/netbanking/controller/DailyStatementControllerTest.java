package com.hdfc.netbanking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.service.DailyStatementService;

class DailyStatementControllerTest {

    private DailyStatementService dailyStatementService;
    private DailyStatementController dailyStatementController;
    private Model model;

    @BeforeEach
    void setUp() {
        dailyStatementService = mock(DailyStatementService.class);

        dailyStatementController =
                new DailyStatementController(dailyStatementService);

        model = mock(Model.class);
    }

    @Test
    void dailyStatement_loadsDataAndReturnsDailyStatementPage() {

        List<Transaction> transactions = List.of(new Transaction());

        when(dailyStatementService.getTodayTransactions())
                .thenReturn(transactions);

        when(dailyStatementService.getTotalTransactions())
                .thenReturn(5);

        when(dailyStatementService.getTotalAmount())
                .thenReturn(2500.0);

        String result =
                dailyStatementController.dailyStatement(model);

        assertEquals("daily-statement", result);

        verify(dailyStatementService)
                .getTodayTransactions();

        verify(dailyStatementService)
                .getTotalTransactions();

        verify(dailyStatementService)
                .getTotalAmount();

        verify(model)
                .addAttribute("transactions", transactions);

        verify(model)
                .addAttribute("totalTransactions", 5);

        verify(model)
                .addAttribute("totalAmount", 2500.0);
    }
}