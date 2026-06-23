package com.example.customer_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CustomerOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerOrderApplication.class, args);
	}

}
