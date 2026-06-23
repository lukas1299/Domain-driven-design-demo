package com.example.manufacturing_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ManufacturingOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManufacturingOrderApplication.class, args);
	}

}
