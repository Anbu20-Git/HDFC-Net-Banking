package com.hdfc.netbanking.service;

import com.hdfc.netbanking.dto.PaymentGatewayResponse;
import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentGatewayClient paymentGatewayClient;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void testCompletePaymentSuccessfully() {

        PaymentGatewayResponse payment = new PaymentGatewayResponse();

        payment.setId("pay_123");
        payment.setAmount(5000.0);
        payment.setCurrency("INR");
        payment.setStatus("success");

        when(paymentGatewayClient.verifyPayment("pay_123"))
                .thenReturn(payment);

        Transaction savedTransaction = new Transaction();

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(savedTransaction);

        Transaction result = paymentService.completePayment(
                "pay_123",
                "501234567890",
                "601234567890"
        );

        assertSame(savedTransaction, result);

        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository, times(1))
                .save(captor.capture());

        Transaction transaction = captor.getValue();

        assertEquals("501234567890",
                transaction.getSenderAccount());

        assertEquals("601234567890",
                transaction.getReceiverAccount());

        assertEquals(5000.0,
                transaction.getAmount());

        assertEquals("ONLINE_PAYMENT",
                transaction.getTransactionType());

        assertEquals("SUCCESS",
                transaction.getStatus());

        verify(paymentGatewayClient, times(1))
                .verifyPayment("pay_123");
    }

    @Test
    void testCompletePaymentWhenPaymentFails() {

        PaymentGatewayResponse payment = new PaymentGatewayResponse();

        payment.setId("pay_456");
        payment.setAmount(3000.0);
        payment.setCurrency("INR");
        payment.setStatus("failed");

        when(paymentGatewayClient.verifyPayment("pay_456"))
                .thenReturn(payment);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentService.completePayment(
                        "pay_456",
                        "501234567890",
                        "601234567890"
                )
        );

        assertEquals(
                "Payment is not successful",
                exception.getMessage()
        );

        verify(paymentGatewayClient, times(1))
                .verifyPayment("pay_456");

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }
}