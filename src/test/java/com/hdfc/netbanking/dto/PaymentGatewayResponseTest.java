package com.hdfc.netbanking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewayResponseTest {

    @Test
    void testNoArgsConstructor() {
        PaymentGatewayResponse response = new PaymentGatewayResponse();

        assertNull(response.getId());
        assertNull(response.getObject());
        assertNull(response.getAmount());
        assertNull(response.getCurrency());
        assertNull(response.getOrder_id());
        assertNull(response.getStatus());
        assertNull(response.getCreated());
    }

    @Test
    void testSettersAndGetters() {
        PaymentGatewayResponse response = new PaymentGatewayResponse();

        response.setId("pay_123");
        response.setObject("payment");
        response.setAmount(5000.0);
        response.setCurrency("INR");
        response.setOrder_id("order_123");
        response.setStatus("success");
        response.setCreated("2026-10-06");

        assertEquals("pay_123", response.getId());
        assertEquals("payment", response.getObject());
        assertEquals(5000.0, response.getAmount());
        assertEquals("INR", response.getCurrency());
        assertEquals("order_123", response.getOrder_id());
        assertEquals("success", response.getStatus());
        assertEquals("2026-10-06", response.getCreated());
    }
}