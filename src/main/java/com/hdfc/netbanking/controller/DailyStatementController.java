package com.hdfc.netbanking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hdfc.netbanking.service.DailyStatementService;

@Controller
public class DailyStatementController {

    private final DailyStatementService dailyStatementService;

    public DailyStatementController(DailyStatementService dailyStatementService) {
    	
        this.dailyStatementService = dailyStatementService;
    }

    @GetMapping("/daily-statement")
    public String dailyStatement(Model model) {

        model.addAttribute("transactions",dailyStatementService.getTodayTransactions());

        model.addAttribute("totalTransactions", dailyStatementService.getTotalTransactions());

        model.addAttribute("totalAmount", dailyStatementService.getTotalAmount());

        return "daily-statement";
    }
}