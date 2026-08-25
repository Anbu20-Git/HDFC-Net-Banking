package com.hdfc.netbanking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hdfc.netbanking.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccountOrReceiverAccount(
            String senderAccount,
            String receiverAccount);

    List<Transaction> findByTransactionDateBetween(
            LocalDateTime start,
            LocalDateTime end);
}