package com.hdfc.netbanking.service;

import org.springframework.stereotype.Service;

import com.hdfc.netbanking.dto.PaymentGatewayResponse;
import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;

@Service
public class PaymentService {

    private final PaymentGatewayClient paymentGatewayClient;

    private final TransactionRepository transactionRepository;

    public PaymentService(
            PaymentGatewayClient paymentGatewayClient,
            TransactionRepository transactionRepository) {

        this.paymentGatewayClient = paymentGatewayClient;
        this.transactionRepository = transactionRepository;
    }

    public Transaction completePayment(
            String paymentId,
            String senderAccount,
            String receiverAccount) {

        // 1. Verify payment with Mock Gateway
        PaymentGatewayResponse payment =
                paymentGatewayClient.verifyPayment(paymentId);

        // 2. Check payment status
        if (!"success".equalsIgnoreCase(payment.getStatus())) {

            throw new RuntimeException(
                    "Payment is not successful"
            );
        }

        // 3. Create HDFC transaction
        Transaction transaction = new Transaction();

        transaction.setSenderAccount(senderAccount);

        transaction.setReceiverAccount(receiverAccount);

        transaction.setAmount(payment.getAmount());

        transaction.setTransactionType("ONLINE_PAYMENT");

        transaction.setStatus("SUCCESS");

        // 4. Save to HDFC transactions table
        return transactionRepository.save(transaction);
    }
}