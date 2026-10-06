package com.hdfc.netbanking.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testNoArgsConstructor() {
        Account account = new Account();

        assertNull(account.getId());
        assertNull(account.getAccountNumber());
        assertEquals(0.0, account.getBalance());
        assertNull(account.getAccountType());
        assertNull(account.getUser());
    }

    @Test
    void testSettersAndGetters() {
        Account account = new Account();

        User user = new User(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210",
                "CUSTOMER"
        );

        account.setId(1L);
        account.setAccountNumber("501234567890");
        account.setBalance(20998.0);
        account.setAccountType("SAVINGS");
        account.setUser(user);

        assertEquals(1L, account.getId());
        assertEquals("501234567890", account.getAccountNumber());
        assertEquals(20998.0, account.getBalance());
        assertEquals("SAVINGS", account.getAccountType());
        assertSame(user, account.getUser());
    }
}