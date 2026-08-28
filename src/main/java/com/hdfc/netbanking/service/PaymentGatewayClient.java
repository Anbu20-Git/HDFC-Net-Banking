package com.hdfc.netbanking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.hdfc.netbanking.dto.PaymentGatewayResponse;
import com.hdfc.netbanking.dto.PaymentRequest;

@Service
public class PaymentGatewayClient {

    private final RestClient restClient;
    private final String apiKey;

    public PaymentGatewayClient(
            RestClient.Builder builder,
            @Value("${mock.gateway.base-url:https://mockgateway.com/api/pg/anbu-selvan-2IQQ8l}") String baseUrl,
            @Value("${mock.gateway.api-key:sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS}") String apiKey) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
        this.apiKey = apiKey;
    }
    
    public PaymentGatewayResponse markPaymentSuccess(
            String paymentId) {

        return restClient
                .post()
                .uri(
                    "/api/pg/anbu-selvan-2IQQ8l/payments/"
                    + paymentId
                    + "/success"
                )
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + this.apiKey
                )
                .retrieve()
                .body(PaymentGatewayResponse.class);
    }
    
    public PaymentGatewayResponse verifyPayment(String paymentId) {

        return restClient
                .get()
                .uri("/api/pg/anbu-selvan-2IQQ8l/verify/" + paymentId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + this.apiKey
                )
                .retrieve()
                .body(PaymentGatewayResponse.class);
    }

    public PaymentGatewayResponse createPayment(
            Double amount,
            String currency,
            String orderId) {

        PaymentRequest request =
                new PaymentRequest(
                        amount,
                        currency,
                        orderId
                );

        return restClient
                .post()
                .uri("/api/pg/anbu-selvan-2IQQ8l/payments")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + this.apiKey
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(PaymentGatewayResponse.class);
    }
}