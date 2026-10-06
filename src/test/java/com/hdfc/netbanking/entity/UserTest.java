package com.hdfc.netbanking.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getPhone());
        assertNull(user.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(
                "Anbu",
                "anbu@gmail.com",
                "password123",
                "9876543210",
                "CUSTOMER"
        );

        assertNull(user.getId());
        assertEquals("Anbu", user.getName());
        assertEquals("anbu@gmail.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("9876543210", user.getPhone());
        assertEquals("CUSTOMER", user.getRole());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setPassword("test123");
        user.setPhone("1234567890");
        user.setRole("ADMIN");

        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("test123", user.getPassword());
        assertEquals("1234567890", user.getPhone());
        assertEquals("ADMIN", user.getRole());
    }
}