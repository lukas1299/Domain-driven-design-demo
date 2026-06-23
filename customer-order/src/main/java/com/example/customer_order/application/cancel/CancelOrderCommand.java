package com.example.customer_order.application.cancel;

import java.util.UUID;

public record CancelOrderCommand (UUID orderId){}
