package com.hdfc.netbanking.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.hdfc.netbanking.dto.PaymentGatewayResponse;
import com.hdfc.netbanking.dto.PaymentRequest;

@Service
public class PaymentGatewayClient {

    private final RestClient restClient;

    private static final String API_KEY =
            "sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS";

    public PaymentGatewayClient(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://localhost:9090")
                .build();
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
                    "Bearer " + API_KEY
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
                        "Bearer " + API_KEY
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
                        "Bearer " + API_KEY
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(PaymentGatewayResponse.class);
    }
}