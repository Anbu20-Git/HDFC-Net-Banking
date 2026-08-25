package com.hdfc.netbanking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hdfc.netbanking.entity.User;
import com.hdfc.netbanking.service.UserService;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
    	
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user,Model model) {

        userService.registerUser(user);

        model.addAttribute("message","Registration successful!");

        return "register";
    }
}