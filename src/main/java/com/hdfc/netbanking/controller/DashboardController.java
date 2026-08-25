package com.hdfc.netbanking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hdfc.netbanking.entity.Account;
import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.repository.AccountRepository;
import com.hdfc.netbanking.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public DashboardController(UserRepository userRepository, AccountRepository accountRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Long userId =(Long) session.getAttribute("userId");

        String role =(String) session.getAttribute("userRole");

        // Not logged in
        if (userId == null) {
        	
            return "redirect:/login";
        }

        // Admin should not access customer dashboard
        if ("ADMIN".equalsIgnoreCase(role)) {
        	
            return "redirect:/admin/dashboard";
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        Account account = accountRepository.findByUserId(userId).orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("account", account);

        return "dashboard";
    }
}