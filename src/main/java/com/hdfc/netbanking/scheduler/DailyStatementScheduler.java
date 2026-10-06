package com.hdfc.netbanking.scheduler;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hdfc.netbanking.entity.Transaction;
import com.hdfc.netbanking.repository.TransactionRepository;
import com.hdfc.netbanking.service.StatementPdfService;

@Component
public class DailyStatementScheduler {
	
	private static final Logger logger =
	        LoggerFactory.getLogger(DailyStatementScheduler.class);

    private final TransactionRepository transactionRepository;
    private final StatementPdfService statementPdfService;

    public DailyStatementScheduler(
            TransactionRepository transactionRepository,
            StatementPdfService statementPdfService) {

        this.transactionRepository = transactionRepository;
        this.statementPdfService = statementPdfService;
    }

    @Scheduled(fixedRate = 60000)
    public void generateDailyStatement() {

        try {

            LocalDate today = LocalDate.now();

            LocalDateTime start = today.atStartOfDay();

            LocalDateTime end =
                    today.plusDays(1).atStartOfDay();

            List<Transaction> transactions =
                    transactionRepository
                            .findByTransactionDateBetween(start, end);

            System.out.println();

            System.out.println("======================================");
            System.out.println("       HDFC DAILY STATEMENT");
            System.out.println("======================================");

            System.out.println("Date: " + today);

            System.out.println(
                    "Total Transactions: "
                    + transactions.size()
            );

            double totalAmount = 0;

            for (Transaction transaction : transactions) {

                totalAmount += transaction.getAmount();

                System.out.println(
                        transaction.getTransactionType()
                        + " | "
                        + transaction.getAmount()
                        + " | "
                        + transaction.getStatus()
                );
            }

            System.out.println(
                    "Total Amount: ₹" + totalAmount
            );

            System.out.println("======================================");

            // Generate PDF
            statementPdfService.generateDailyStatementPdf();

            System.out.println(
                    "PDF Generated Successfully"
            );

            System.out.println();

        } catch (Exception e) {

            System.out.println(
                    "Error while generating daily statement PDF"
            );

            logger.error("Error while generating daily statement PDF", e);
        }
    }
}