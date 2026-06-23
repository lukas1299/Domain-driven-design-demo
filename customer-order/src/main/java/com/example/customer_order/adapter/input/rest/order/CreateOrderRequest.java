package com.example.customer_order.adapter.input.rest.order;

import java.util.UUID;

public record CreateOrderRequest(UUID customerId, String productSku, int quantity) {}


