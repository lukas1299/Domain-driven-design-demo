package com.example.customer_order.application.order;

import java.util.UUID;

public record UpdateCustomerOrderCommand(UUID orderId, String productSku, int quantity) {}
