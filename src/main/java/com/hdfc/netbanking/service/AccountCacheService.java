package com.hdfc.netbanking.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hdfc.netbanking.entity.Account;

@Service
public class AccountCacheService {

    private final RedisTemplate<String, Account> redisTemplate;

    public AccountCacheService(RedisTemplate<String, Account> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveAccount(Long userId, Account account) {

        String key = "account:" + userId;

        redisTemplate.opsForValue().set(
                key,
                account,
                Duration.ofMinutes(10)
        );

        System.out.println("Account saved to Redis");
    }

    public Account getAccount(Long userId) {

        String key = "account:" + userId;

        return redisTemplate.opsForValue().get(key);
    }

    public void deleteAccount(Long userId) {

        String key = "account:" + userId;

        redisTemplate.delete(key);

        System.out.println("Account removed from Redis");
    }
}