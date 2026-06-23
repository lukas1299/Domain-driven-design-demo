package com.example.manufacturing_order.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.example.manufacturing_order")
@EnableJpaRepositories(basePackages = "com.example.manufacturing_order")
public class ManufacturingJpaConfig {
}
