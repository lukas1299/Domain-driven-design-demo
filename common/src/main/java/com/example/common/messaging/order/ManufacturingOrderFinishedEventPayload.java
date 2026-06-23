package com.example.common.messaging.order;

public record ManufacturingOrderFinishedEventPayload(
        String manufacturingOrderId,
        String sourceOrderId,
        String productSku,
        int quantity,
        String status
) {}
