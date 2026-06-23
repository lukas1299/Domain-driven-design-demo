package com.example.customer_order.application.order;

public record CreateCustomerOrderCommand(
        String customerId,
        String productSku,
        int quantity
) {}