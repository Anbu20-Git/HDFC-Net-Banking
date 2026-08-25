package com.hdfc.netbanking.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.netbanking.service.StatementPdfService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class StatementController {

    private final StatementPdfService statementPdfService;

    public StatementController(StatementPdfService statementPdfService) {
        this.statementPdfService = statementPdfService;
    }

    @GetMapping("/statement/download")
    public ResponseEntity<byte[]> downloadStatement() throws Exception {

        byte[] pdf =
                statementPdfService.generateDailyStatementPdf();

        String time =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd_HH-mm-ss"));

        String fileName =
                "HDFC_Statement_" + time + ".pdf";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}