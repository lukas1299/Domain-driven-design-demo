package com.example.customer_order.application.order;

import java.util.UUID;

public record DeleteCustomerOrderCommand(UUID orderId) {}
