package com.example.manufacturing_order.application.order;

public record CreateManufacturingOrderCommand(String sourceOrderId, String productSku, int quantity) {}
