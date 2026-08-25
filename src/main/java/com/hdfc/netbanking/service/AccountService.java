package com.hdfc.netbanking.service;

import org.springframework.stereotype.Service;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountCacheService accountCacheService;

    public AccountService(AccountRepository accountRepository,
                          AccountCacheService accountCacheService) {

        this.accountRepository = accountRepository;
        this.accountCacheService = accountCacheService;
    }

    public Account getAccountByUserId(Long userId) {

        Account cachedAccount = accountCacheService.getAccount(userId);

        if (cachedAccount != null) {
            System.out.println("Account loaded from Redis");
            return cachedAccount;
        }

        Account account = accountRepository.findByUserId(userId)
                .orElse(null);

        if (account != null) {
            accountCacheService.saveAccount(userId, account);
        }

        return account;
    }
}