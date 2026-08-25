package com.hdfc.netbanking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;

@Service
public class DailyStatementService {

    private final TransactionRepository transactionRepository;

    public DailyStatementService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTodayTransactions() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();

        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return transactionRepository
                .findByTransactionDateBetween(startOfDay, endOfDay);
    }

    public double getTotalAmount() {

        return getTodayTransactions()
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public int getTotalTransactions() {

        return getTodayTransactions().size();
    }
}