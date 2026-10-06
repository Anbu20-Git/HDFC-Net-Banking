package com.hdfc.netbanking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestTest {

    @Test
    void testNoArgsConstructor() {
        PaymentRequest request = new PaymentRequest();

        assertNull(request.getAmount());
        assertNull(request.getCurrency());
        assertNull(request.getOrder_id());
    }

    @Test
    void testAllArgsConstructor() {
        PaymentRequest request = new PaymentRequest(
                5000.0,
                "INR",
                "order_123"
        );

        assertEquals(5000.0, request.getAmount());
        assertEquals("INR", request.getCurrency());
        assertEquals("order_123", request.getOrder_id());
    }

    @Test
    void testSettersAndGetters() {
        PaymentRequest request = new PaymentRequest();

        request.setAmount(10000.0);
        request.setCurrency("INR");
        request.setOrder_id("order_456");

        assertEquals(10000.0, request.getAmount());
        assertEquals("INR", request.getCurrency());
        assertEquals("order_456", request.getOrder_id());
    }
}