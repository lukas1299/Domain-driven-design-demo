package com.example.customer_order.adapter.output.messaging.payment;

import java.util.UUID;

public record CustomerOrderPaidDomainEvent(
        UUID orderId,
        UUID customerId,
        String productSku,
        int quantity
) {}