package com.example.customer_order.application.payment;

import java.util.UUID;

public record PayCustomerOrderCommand(UUID orderId) {}