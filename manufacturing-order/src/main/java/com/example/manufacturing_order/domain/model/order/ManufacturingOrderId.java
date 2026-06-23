package com.example.manufacturing_order.domain.model.order;

import java.util.UUID;

public record ManufacturingOrderId(UUID value) {
    public static ManufacturingOrderId generate() {
        return new ManufacturingOrderId(UUID.randomUUID());
    }
}
