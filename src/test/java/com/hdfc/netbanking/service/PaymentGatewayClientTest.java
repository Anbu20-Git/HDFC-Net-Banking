package com.hdfc.netbanking.service;

import com.hdfc.netbanking.dto.PaymentGatewayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentGatewayClientTest {

    @Mock
    private RestClient.Builder builder;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private PaymentGatewayClient paymentGatewayClient;

    @BeforeEach
    void setUp() {

        when(builder.baseUrl("http://localhost:9090"))
                .thenReturn(builder);

        when(builder.build())
                .thenReturn(restClient);

        paymentGatewayClient =
                new PaymentGatewayClient(builder);
    }

    @Test
    void testMarkPaymentSuccess() {

        PaymentGatewayResponse response =
                new PaymentGatewayResponse();

        response.setId("pay_123");
        response.setStatus("success");
        response.setAmount(5000.0);

        when(restClient.post())
                .thenReturn(requestBodyUriSpec);

        when(requestBodyUriSpec.uri(
                "/api/pg/anbu-selvan-2IQQ8l/payments/pay_123/success"))
                .thenReturn(requestBodySpec);

        when(requestBodySpec.header(
                HttpHeaders.AUTHORIZATION,
                "Bearer sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS"))
                .thenReturn(requestBodySpec);

        when(requestBodySpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(PaymentGatewayResponse.class))
                .thenReturn(response);

        PaymentGatewayResponse result =
                paymentGatewayClient.markPaymentSuccess("pay_123");

        assertSame(response, result);

        verify(restClient).post();

        verify(requestBodyUriSpec).uri(
                "/api/pg/anbu-selvan-2IQQ8l/payments/pay_123/success"
        );

        verify(requestBodySpec).header(
                HttpHeaders.AUTHORIZATION,
                "Bearer sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS"
        );

        verify(requestBodySpec).retrieve();

        verify(responseSpec).body(PaymentGatewayResponse.class);
    }

    @Test
    void testVerifyPayment() {

        PaymentGatewayResponse response =
                new PaymentGatewayResponse();

        response.setId("pay_456");
        response.setStatus("success");
        response.setAmount(3000.0);

        when(restClient.get())
                .thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(
                "/api/pg/anbu-selvan-2IQQ8l/verify/pay_456"))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.header(
                HttpHeaders.AUTHORIZATION,
                "Bearer sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS"))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(PaymentGatewayResponse.class))
                .thenReturn(response);

        PaymentGatewayResponse result =
                paymentGatewayClient.verifyPayment("pay_456");

        assertSame(response, result);

        verify(restClient).get();

        verify(requestHeadersUriSpec).uri(
                "/api/pg/anbu-selvan-2IQQ8l/verify/pay_456"
        );

        verify(requestHeadersSpec).header(
                HttpHeaders.AUTHORIZATION,
                "Bearer sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS"
        );

        verify(requestHeadersSpec).retrieve();

        verify(responseSpec).body(PaymentGatewayResponse.class);
    }

    @Test
    void testCreatePayment() {

        PaymentGatewayResponse response =
                new PaymentGatewayResponse();

        response.setId("pay_789");
        response.setStatus("success");
        response.setAmount(10000.0);

        when(restClient.post())
                .thenReturn(requestBodyUriSpec);

        when(requestBodyUriSpec.uri(
                "/api/pg/anbu-selvan-2IQQ8l/payments"))
                .thenReturn(requestBodySpec);

        when(requestBodySpec.header(
                HttpHeaders.AUTHORIZATION,
                "Bearer sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS"))
                .thenReturn(requestBodySpec);

        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON))
                .thenReturn(requestBodySpec);

        when(requestBodySpec.body(any(com.hdfc.netbanking.dto.PaymentRequest.class)))
        .thenReturn(requestBodySpec);
        
        when(requestBodySpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(PaymentGatewayResponse.class))
                .thenReturn(response);

        PaymentGatewayResponse result =
                paymentGatewayClient.createPayment(
                        10000.0,
                        "INR",
                        "order_123"
                );

        assertSame(response, result);

        verify(restClient).post();

        verify(requestBodyUriSpec).uri(
                "/api/pg/anbu-selvan-2IQQ8l/payments"
        );

        verify(requestBodySpec).header(
                HttpHeaders.AUTHORIZATION,
                "Bearer sk_test_a7uRFh08FhkNmwmAgcRZZHlJyuuIQenS"
        );

        verify(requestBodySpec)
                .contentType(MediaType.APPLICATION_JSON);

        verify(requestBodySpec).body(any(com.hdfc.netbanking.dto.PaymentRequest.class));

        verify(requestBodySpec).retrieve();

        verify(responseSpec).body(PaymentGatewayResponse.class);
    }
}