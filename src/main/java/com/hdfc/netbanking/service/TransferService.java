package com.hdfc.netbanking.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.TransactionRepository;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public String transferMoney(
            String senderAccountNumber,
            String receiverAccountNumber,
            double amount) {

        // Check amount
        if (amount <= 0) {
            return "Amount must be greater than zero";
        }

        // Find sender
        Account sender = accountRepository
                .findByAccountNumber(senderAccountNumber)
                .orElse(null);

        if (sender == null) {
            return "Sender account not found";
        }

        // Find receiver
        Account receiver = accountRepository
                .findByAccountNumber(receiverAccountNumber)
                .orElse(null);

        if (receiver == null) {
            return "Receiver account not found";
        }

        // Prevent self transfer
        if (senderAccountNumber.equals(receiverAccountNumber)) {
            return "You cannot transfer money to your own account";
        }

        // Check balance
        if (sender.getBalance() < amount) {
            return "Insufficient balance";
        }

        // Deduct from sender
        sender.setBalance(sender.getBalance() - amount);

        // Add to receiver
        receiver.setBalance(receiver.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Create transaction
        Transaction transaction = new Transaction();

        transaction.setSenderAccount(senderAccountNumber);
        transaction.setReceiverAccount(receiverAccountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType("TRANSFER");
        transaction.setStatus("SUCCESS");
        transaction.setTransactionDate(
                LocalDateTime.now(ZoneId.of("Asia/Kolkata")));

        transactionRepository.save(transaction);

        return "Transfer successful";
    }
}