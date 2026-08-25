package com.hdfc.netbanking.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.TransactionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionController(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/transactions")
    public String transactionHistory(HttpSession session,Model model) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        Account account = accountRepository
                .findByUserId(userId)
                .orElse(null);

        if (account == null) {
            return "redirect:/dashboard";
        }

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderAccountOrReceiverAccount(
                                account.getAccountNumber(),
                                account.getAccountNumber());

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);

        return "transactions";
    }
}