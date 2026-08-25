package com.hdfc.netbanking;

import org.springframework.boot.SpringApplication;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class HdfcNetBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HdfcNetBankingApplication.class, args);
	}

}
