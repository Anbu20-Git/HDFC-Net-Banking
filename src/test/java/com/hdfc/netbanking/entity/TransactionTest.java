package com.hdfc.netbanking.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testNoArgsConstructor() {
        Transaction transaction = new Transaction();

        assertNull(transaction.getId());
        assertNull(transaction.getSenderAccount());
        assertNull(transaction.getReceiverAccount());
        assertEquals(0.0, transaction.getAmount());
        assertNull(transaction.getTransactionType());
        assertNull(transaction.getStatus());
        assertNull(transaction.getTransactionDate());
    }

    @Test
    void testSettersAndGetters() {
        Transaction transaction = new Transaction();

        LocalDateTime date = LocalDateTime.of(2026, 10, 6, 14, 0);

        transaction.setId(1L);
        transaction.setSenderAccount("501234567890");
        transaction.setReceiverAccount("601234567890");
        transaction.setAmount(5000.0);
        transaction.setTransactionType("TRANSFER");
        transaction.setStatus("SUCCESS");
        transaction.setTransactionDate(date);

        assertEquals(1L, transaction.getId());
        assertEquals("501234567890", transaction.getSenderAccount());
        assertEquals("601234567890", transaction.getReceiverAccount());
        assertEquals(5000.0, transaction.getAmount());
        assertEquals("TRANSFER", transaction.getTransactionType());
        assertEquals("SUCCESS", transaction.getStatus());
        assertEquals(date, transaction.getTransactionDate());
    }

    @Test
    void testSetTransactionDateWhenDateIsNull() {
        Transaction transaction = new Transaction();

        assertNull(transaction.getTransactionDate());

        transaction.setTransactionDate();

        assertNotNull(transaction.getTransactionDate());
    }

    @Test
    void testSetTransactionDateWhenDateAlreadyExists() {
        Transaction transaction = new Transaction();

        LocalDateTime existingDate = LocalDateTime.of(2026, 10, 6, 14, 0);

        transaction.setTransactionDate(existingDate);

        transaction.setTransactionDate();

        assertEquals(existingDate, transaction.getTransactionDate());
    }
}