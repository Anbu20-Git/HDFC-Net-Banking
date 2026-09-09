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

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public LoginController(UserRepository userRepository, AccountRepository accountRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user = userRepository.findByEmail(email).orElse(null);
        
        System.out.println("LOGIN EMAIL RECEIVED: [" + email + "]");
        System.out.println("USER FOUND: " + (user != null));
        System.out.println("ENTERED PASSWORD LENGTH: " + password.length());

        // Invalid login
        if (user == null ||
            !user.getPassword().equals(password)) {

            model.addAttribute("error","Invalid email or password");

            return "login";
        }

        // Save logged-in user in session
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("userName", user.getName());

        // ADMIN
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {

            return "redirect:/admin/dashboard";
        }

        // USER
        return "redirect:/dashboard";
    }
}