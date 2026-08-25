package com.hdfc.netbanking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.UserRepository;
import com.hdfc.netbanking.service.TransferService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransferController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransferService transferService;

    public TransferController(
            UserRepository userRepository,
            AccountRepository accountRepository,
            TransferService transferService) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transferService = transferService;
    }

    @GetMapping("/transfer")
    public String transferPage(
            HttpSession session,
            Model model) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        Account account = accountRepository
                .findByUserId(userId)
                .orElse(null);

        User user = userRepository
                .findById(userId)
                .orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("account", account);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String receiverAccount,
            @RequestParam double amount,
            HttpSession session,
            Model model) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        Account sender = accountRepository
                .findByUserId(userId)
                .orElse(null);

        if (sender == null) {
            return "redirect:/dashboard";
        }

        String result =
                transferService.transferMoney(
                        sender.getAccountNumber(),
                        receiverAccount,
                        amount
                );

        Account updatedAccount =
                accountRepository
                        .findByUserId(userId)
                        .orElse(null);

        User user =
                userRepository
                        .findById(userId)
                        .orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("account", updatedAccount);
        model.addAttribute("message", result);

        return "transfer";
    }
}