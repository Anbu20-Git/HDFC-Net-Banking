package com.hdfc.netbanking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MockPaymentServiceTest {

    @org.mockito.Mock
    private WebClient.Builder webClientBuilder;

    @org.mockito.Mock
    private WebClient webClient;

    @org.mockito.Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @org.mockito.Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @org.mockito.Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @org.mockito.Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @org.mockito.Mock
    private WebClient.ResponseSpec responseSpec;

    private MockPaymentService mockPaymentService;

    @BeforeEach
    void setUp() {

        when(webClientBuilder.baseUrl("http://localhost:9090"))
                .thenReturn(webClientBuilder);

        when(webClientBuilder.build())
                .thenReturn(webClient);

        mockPaymentService =
                new MockPaymentService(
                        webClientBuilder,
                        "http://localhost:9090"
                );

        ReflectionTestUtils.setField(
                mockPaymentService,
                "apiKey",
                "test-api-key"
        );
    }

    @Test
    void testInitiatePayment() {

        Map<String, Object> response =
                Map.of(
                        "id", "pay_123",
                        "status", "success",
                        "amount", 5000.0
                );

        when(webClient.post())
                .thenReturn(requestBodyUriSpec);

        when(requestBodyUriSpec.uri("/init"))
                .thenReturn(requestBodySpec);

        when(requestBodySpec.header(
                "Authorization",
                "Bearer test-api-key"
        )).thenReturn(requestBodySpec);

        when(requestBodySpec.contentType(
                org.springframework.http.MediaType.APPLICATION_JSON
        )).thenReturn(requestBodySpec);

        when(requestBodySpec.bodyValue(any()))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(response));

        Map<String, Object> result =
                mockPaymentService.initiatePayment(
                        5000.0,
                        "INR",
                        "order_123"
                );

        assertNotNull(result);
        assertEquals("pay_123", result.get("id"));
        assertEquals("success", result.get("status"));
        assertEquals(5000.0, result.get("amount"));

        verify(webClient).post();

        verify(requestBodyUriSpec)
                .uri("/init");

        verify(requestBodySpec)
                .header(
                        "Authorization",
                        "Bearer test-api-key"
                );

        verify(requestBodySpec)
                .contentType(
                        org.springframework.http.MediaType.APPLICATION_JSON
                );

        verify(requestBodySpec)
                .bodyValue(any());

        verify(requestHeadersSpec)
                .retrieve();

        verify(responseSpec)
                .bodyToMono(Map.class);
    }

    @Test
    void testVerifyPayment() {

        Map<String, Object> response =
                Map.of(
                        "id", "pay_456",
                        "status", "success",
                        "amount", 3000.0
                );

        when(webClient.get())
                .thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri("/verify/pay_456"))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.header(
                "Authorization",
                "Bearer test-api-key"
        )).thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(response));

        Map<String, Object> result =
                mockPaymentService.verifyPayment("pay_456");

        assertNotNull(result);
        assertEquals("pay_456", result.get("id"));
        assertEquals("success", result.get("status"));
        assertEquals(3000.0, result.get("amount"));

        verify(webClient).get();

        verify(requestHeadersUriSpec)
                .uri("/verify/pay_456");

        verify(requestHeadersSpec)
                .header(
                        "Authorization",
                        "Bearer test-api-key"
                );

        verify(requestHeadersSpec)
                .retrieve();

        verify(responseSpec)
                .bodyToMono(Map.class);
    }
}