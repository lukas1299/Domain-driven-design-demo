package com.example.common.messaging.order;

public record OrderCreatedEventPayload(String orderId, String sku, int qty) {}
