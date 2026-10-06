package com.hdfc.netbanking.controller;

import com.hdfc.netbanking.service.MockPaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private MockPaymentService mockPaymentService;

    @Mock
    private Model model;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void testPaymentPage() {

        String result =
                paymentController.paymentPage();

        assertEquals("payment", result);
    }

    @Test
    void testInitiatePaymentSuccessfully() {

        Map<String, Object> response =
                Map.of(
                        "id", "pay_123",
                        "payment_url",
                        "http://localhost:9090/payment/pay_123"
                );

        when(mockPaymentService.initiatePayment(
                5000.0,
                "INR",
                "order_123"
        )).thenReturn(response);

        RedirectView result =
                paymentController.initiatePayment(
                        5000.0,
                        "order_123"
                );

        assertNotNull(result);

        assertEquals(
                "http://localhost:9090/payment/pay_123",
                result.getUrl()
        );

        verify(mockPaymentService)
                .initiatePayment(
                        5000.0,
                        "INR",
                        "order_123"
                );
    }

    @Test
    void testInitiatePaymentWhenServiceFails() {

        when(mockPaymentService.initiatePayment(
                5000.0,
                "INR",
                "order_123"
        )).thenThrow(
                new RuntimeException("Gateway unavailable")
        );

        RedirectView result =
                paymentController.initiatePayment(
                        5000.0,
                        "order_123"
                );

        assertNotNull(result);

        assertEquals(
                "/payment?error=Payment initiation failed",
                result.getUrl()
        );

        verify(mockPaymentService)
                .initiatePayment(
                        5000.0,
                        "INR",
                        "order_123"
                );
    }

    @Test
    void testVerifyPaymentSuccessfully() {

        Map<String, Object> response =
                Map.of(
                        "id", "pay_123",
                        "status", "success",
                        "amount", 5000.0
                );

        when(mockPaymentService.verifyPayment("pay_123"))
                .thenReturn(response);

        String result =
                paymentController.verifyPayment(
                        "pay_123",
                        model
                );

        assertEquals("payment-result", result);

        verify(mockPaymentService)
                .verifyPayment("pay_123");

        verify(model)
                .addAttribute(
                        "paymentId",
                        "pay_123"
                );

        verify(model)
                .addAttribute(
                        "payment",
                        response
                );
    }

    @Test
    void testVerifyPaymentWhenServiceFails() {

        when(mockPaymentService.verifyPayment("pay_456"))
                .thenThrow(
                        new RuntimeException("Payment not found")
                );

        String result =
                paymentController.verifyPayment(
                        "pay_456",
                        model
                );

        assertEquals("payment-result", result);

        verify(mockPaymentService)
                .verifyPayment("pay_456");

        verify(model)
                .addAttribute(
                        "error",
                        "Payment verification failed: Payment not found"
                );
    }
}