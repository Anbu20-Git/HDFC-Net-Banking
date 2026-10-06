package com.hdfc.netbanking.service;

import com.hdfc.netbanking.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountCacheServiceTest {

    @Mock
    private RedisTemplate<String, Account> redisTemplate;

    @Mock
    private ValueOperations<String, Account> valueOperations;

    @InjectMocks
    private AccountCacheService accountCacheService;

    @Test
    void testSaveAccount() {

        Long userId = 1L;

        Account account = new Account();
        account.setId(10L);
        account.setAccountNumber("501234567890");
        account.setBalance(20000.0);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        accountCacheService.saveAccount(userId, account);

        verify(redisTemplate, times(1)).opsForValue();

        verify(valueOperations, times(1)).set(
                "account:1",
                account,
                Duration.ofMinutes(10)
        );
    }

    @Test
    void testGetAccount() {

        Long userId = 2L;

        Account account = new Account();
        account.setId(20L);
        account.setAccountNumber("601234567890");
        account.setBalance(15000.0);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("account:2")).thenReturn(account);

        Account result = accountCacheService.getAccount(userId);

        assertSame(account, result);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get("account:2");
    }

    @Test
    void testGetAccountWhenNotFound() {

        Long userId = 3L;

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("account:3")).thenReturn(null);

        Account result = accountCacheService.getAccount(userId);

        assertNull(result);

        verify(valueOperations, times(1)).get("account:3");
    }

    @Test
    void testDeleteAccount() {

        Long userId = 4L;

        accountCacheService.deleteAccount(userId);

        verify(redisTemplate, times(1))
                .delete("account:4");
    }
}