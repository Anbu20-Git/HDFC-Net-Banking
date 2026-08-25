package com.hdfc.netbanking.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MockPaymentService {

    private final WebClient webClient;

    @Value("${mock.gateway.api-key}")
    private String apiKey;

    public MockPaymentService(
            WebClient.Builder webClientBuilder,
            @Value("${mock.gateway.base-url}") String baseUrl) {

        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    // -------------------------------------------------
    // INITIATE PAYMENT
    // -------------------------------------------------

    public Map<String, Object> initiatePayment(
            double amount,
            String currency,
            String orderId) {

        return webClient
                .post()
                .uri("/init")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "amount", amount,
                        "currency", currency,
                        "order_id", orderId
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    // -------------------------------------------------
    // VERIFY PAYMENT
    // -------------------------------------------------

    public Map<String, Object> verifyPayment(String paymentId) {

        return webClient
                .get()
                .uri("/verify/" + paymentId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey
                )
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}