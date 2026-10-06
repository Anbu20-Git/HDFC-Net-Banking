package com.hdfc.netbanking.service;

import java.io.ByteArrayOutputStream;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.Cell;
import com.lowagie.text.pdf.PdfWriter;

@Service   
public class StatementPdfService {

    private final TransactionRepository transactionRepository;

    public StatementPdfService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public byte[] generateDailyStatementPdf() throws Exception {

    	LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));

        LocalDateTime start = today.atStartOfDay();

        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<Transaction> transactions =
                transactionRepository.findByTransactionDateBetween(start, end);

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        document.add(new Paragraph("HDFC DAILY STATEMENT"));

        document.add(new Paragraph("Date: " + today));

        String generatedTime =
        		LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        document.add(new Paragraph(
                "Generated On: " + generatedTime));

        document.add(new Paragraph(" "));

        Table table = new Table(6);

        table.addCell(new Cell("ID"));
        table.addCell(new Cell("Sender"));
        table.addCell(new Cell("Receiver"));
        table.addCell(new Cell("Amount"));
        table.addCell(new Cell("Type"));
        table.addCell(new Cell("Status"));

        double totalAmount = 0;

        for (Transaction transaction : transactions) {

            table.addCell(new Cell(
                    String.valueOf(transaction.getId())));

            table.addCell(new Cell(
                    transaction.getSenderAccount()));

            table.addCell(new Cell(
                    transaction.getReceiverAccount()));

            table.addCell(new Cell(
                    "₹" + transaction.getAmount()));

            table.addCell(new Cell(
                    transaction.getTransactionType()));

            table.addCell(new Cell(
                    transaction.getStatus()));

            totalAmount += transaction.getAmount();
        }

        document.add(table);

        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "Total Transactions: " + transactions.size()));

        document.add(new Paragraph(
                "Total Amount: ₹" + totalAmount));

        document.close();

        return outputStream.toByteArray();
    }
}   