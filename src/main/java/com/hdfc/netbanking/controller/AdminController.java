package com.hdfc.netbanking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.TransactionRepository;
import com.hdfc.netbanking.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AdminController(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session,Model model) {

        Long userId = (Long) session.getAttribute("userId");

        String role = (String) session.getAttribute("userRole");

        // Not logged in
        if (userId == null) {
        	
            return "redirect:/login";
        }

        // Not ADMIN
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("users", userRepository.findAll());

        model.addAttribute("accounts", accountRepository.findAll());

        model.addAttribute("transactions", transactionRepository.findAll());

        return "admin-dashboard";
    }
}