package com.hdfc.netbanking.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.hdfc.netbanking.service.MockPaymentService;

@Controller
public class PaymentController {

    private final MockPaymentService mockPaymentService;

    public PaymentController(MockPaymentService mockPaymentService) {
    	
        this.mockPaymentService = mockPaymentService;
    }

    // -------------------------------------------------
    // SHOW PAYMENT PAGE
    // -------------------------------------------------

    @GetMapping("/payment")
    public String paymentPage() {
        return "payment";
    }

    // -------------------------------------------------
    // INITIATE PAYMENT
    // -------------------------------------------------

    @PostMapping("/payment/initiate")
    public RedirectView initiatePayment(
            @RequestParam double amount,
            @RequestParam String orderId) {

        try {

            Map<String, Object> response = mockPaymentService.initiatePayment(amount,"INR",orderId);

            System.out.println("Mock Gateway Response:");
            System.out.println(response);

            String paymentId = (String) response.get("id");

            String paymentUrl = (String) response.get("payment_url");

            System.out.println("REAL PAYMENT ID: " + paymentId);
            System.out.println("PAYMENT URL: " + paymentUrl);

            /*
             * Save paymentId somewhere if you want
             * to verify it later.
             */

            return new RedirectView(paymentUrl);

        } catch (Exception e) {

            e.printStackTrace();

            return new RedirectView("/payment?error=Payment initiation failed");
        }
    }

    // -------------------------------------------------
    // VERIFY PAYMENT
    // -------------------------------------------------

    @GetMapping("/payment/verify")
    public String verifyPayment(
            @RequestParam String paymentId,Model model) {

        try {

            Map<String, Object> response = mockPaymentService.verifyPayment(paymentId);

            System.out.println("Verification Response:");
            System.out.println(response);

            model.addAttribute("paymentId", paymentId);
            model.addAttribute("payment", response);

            return "payment-result";

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("error","Payment verification failed: " + e.getMessage());

            return "payment-result";
        }
    }
}