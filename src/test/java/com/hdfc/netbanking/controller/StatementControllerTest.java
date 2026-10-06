package com.hdfc.netbanking.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.hdfc.netbanking.service.StatementPdfService;

class StatementControllerTest {

    private StatementPdfService statementPdfService;
    private StatementController statementController;

    @BeforeEach
    void setUp() {
        statementPdfService = mock(StatementPdfService.class);
        statementController = new StatementController(statementPdfService);
    }

    @Test
    void downloadStatement_returnsPdfResponse() throws Exception {

        byte[] pdfBytes = "%PDF-test-content".getBytes();

        when(statementPdfService.generateDailyStatementPdf())
                .thenReturn(pdfBytes);

        ResponseEntity<byte[]> response =
                statementController.downloadStatement();

        assertEquals(200, response.getStatusCode().value());

        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());

        String contentDisposition =
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);

        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.startsWith("attachment; filename=\"HDFC_Statement_"));
        assertTrue(contentDisposition.endsWith(".pdf\""));

        assertArrayEquals(pdfBytes, response.getBody());

        verify(statementPdfService).generateDailyStatementPdf();
    }
}