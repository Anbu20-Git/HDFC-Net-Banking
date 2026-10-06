package com.hdfc.netbanking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hdfc.netbanking.dto.RegisterRequest;
import com.hdfc.netbanking.service.UserService;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") RegisterRequest request,
            Model model) {

        userService.registerUser(request);

        model.addAttribute("message", "Registration successful!");

        return "register";
    }
}