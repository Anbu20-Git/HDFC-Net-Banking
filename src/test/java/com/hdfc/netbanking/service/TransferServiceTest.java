package com.hdfc.netbanking.service;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void testTransferWithInvalidAmount() {

        String result = transferService.transferMoney(
                "501234567890",
                "601234567890",
                0
        );

        assertEquals("Amount must be greater than zero", result);

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testTransferWhenSenderNotFound() {

        when(accountRepository.findByAccountNumber("501234567890"))
                .thenReturn(Optional.empty());

        String result = transferService.transferMoney(
                "501234567890",
                "601234567890",
                5000
        );

        assertEquals("Sender account not found", result);

        verify(accountRepository, times(1))
                .findByAccountNumber("501234567890");

        verify(accountRepository, never())
                .save(any(Account.class));

        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testTransferWhenReceiverNotFound() {

        Account sender = new Account();
        sender.setAccountNumber("501234567890");
        sender.setBalance(20000);

        when(accountRepository.findByAccountNumber("501234567890"))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByAccountNumber("601234567890"))
                .thenReturn(Optional.empty());

        String result = transferService.transferMoney(
                "501234567890",
                "601234567890",
                5000
        );

        assertEquals("Receiver account not found", result);

        verify(accountRepository, times(1))
                .findByAccountNumber("501234567890");

        verify(accountRepository, times(1))
                .findByAccountNumber("601234567890");

        verify(accountRepository, never())
                .save(any(Account.class));

        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testSelfTransfer() {

        Account sender = new Account();
        sender.setAccountNumber("501234567890");
        sender.setBalance(20000);

        when(accountRepository.findByAccountNumber("501234567890"))
                .thenReturn(Optional.of(sender));

        String result = transferService.transferMoney(
                "501234567890",
                "501234567890",
                5000
        );

        assertEquals(
                "You cannot transfer money to your own account",
                result
        );

        verify(accountRepository, times(2))
                .findByAccountNumber("501234567890");

        verify(accountRepository, never())
                .save(any(Account.class));

        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testTransferWithInsufficientBalance() {

        Account sender = new Account();
        sender.setAccountNumber("501234567890");
        sender.setBalance(3000);

        Account receiver = new Account();
        receiver.setAccountNumber("601234567890");
        receiver.setBalance(10000);

        when(accountRepository.findByAccountNumber("501234567890"))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByAccountNumber("601234567890"))
                .thenReturn(Optional.of(receiver));

        String result = transferService.transferMoney(
                "501234567890",
                "601234567890",
                5000
        );

        assertEquals("Insufficient balance", result);

        assertEquals(3000, sender.getBalance());
        assertEquals(10000, receiver.getBalance());

        verify(accountRepository, never())
                .save(any(Account.class));

        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testSuccessfulTransfer() {

        Account sender = new Account();
        sender.setAccountNumber("501234567890");
        sender.setBalance(20000);

        Account receiver = new Account();
        receiver.setAccountNumber("601234567890");
        receiver.setBalance(10000);

        when(accountRepository.findByAccountNumber("501234567890"))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByAccountNumber("601234567890"))
                .thenReturn(Optional.of(receiver));

        String result = transferService.transferMoney(
                "501234567890",
                "601234567890",
                5000
        );

        assertEquals("Transfer successful", result);

        assertEquals(15000, sender.getBalance());
        assertEquals(15000, receiver.getBalance());

        verify(accountRepository, times(1)).save(sender);
        verify(accountRepository, times(1)).save(receiver);

        verify(transactionRepository, times(1))
                .save(any(Transaction.class));
    }
}