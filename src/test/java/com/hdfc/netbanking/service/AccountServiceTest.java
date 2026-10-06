package com.hdfc.netbanking.service;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountCacheService accountCacheService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void testGetAccountFromCache() {
        Long userId = 1L;

        Account cachedAccount = new Account();
        cachedAccount.setId(10L);
        cachedAccount.setAccountNumber("501234567890");
        cachedAccount.setBalance(20000.0);

        when(accountCacheService.getAccount(userId))
                .thenReturn(cachedAccount);

        Account result = accountService.getAccountByUserId(userId);

        assertSame(cachedAccount, result);

        verify(accountCacheService, times(1)).getAccount(userId);
        verify(accountRepository, never()).findByUserId(anyLong());
        verify(accountCacheService, never()).saveAccount(anyLong(), any(Account.class));
    }

    @Test
    void testGetAccountFromDatabaseWhenCacheMiss() {
        Long userId = 2L;

        Account account = new Account();
        account.setId(20L);
        account.setAccountNumber("601234567890");
        account.setBalance(15000.0);

        when(accountCacheService.getAccount(userId))
                .thenReturn(null);

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.of(account));

        Account result = accountService.getAccountByUserId(userId);

        assertSame(account, result);

        verify(accountRepository, times(1)).findByUserId(userId);
        verify(accountCacheService, times(1))
                .saveAccount(userId, account);
    }

    @Test
    void testGetAccountWhenAccountDoesNotExist() {
        Long userId = 3L;

        when(accountCacheService.getAccount(userId))
                .thenReturn(null);

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        Account result = accountService.getAccountByUserId(userId);

        assertNull(result);

        verify(accountRepository, times(1)).findByUserId(userId);
        verify(accountCacheService, never())
                .saveAccount(anyLong(), any(Account.class));
    }
}