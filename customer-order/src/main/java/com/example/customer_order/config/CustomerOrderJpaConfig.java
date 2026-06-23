package com.example.customer_order.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.example.customer_order")
@EnableJpaRepositories(basePackages = "com.example.customer_order")
public class CustomerOrderJpaConfig {
}