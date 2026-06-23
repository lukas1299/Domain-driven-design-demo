package com.example.customer_order.domain.model.order;

import java.util.UUID;

public record OrderId(UUID value) {
    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
    public static OrderId of(UUID id) {
        return new OrderId(id);
    }
}