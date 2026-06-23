package com.example.customer_order.domain.model.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Nie znaleziono zamówienia")
public class CustomerOrderNotFoundException extends RuntimeException {
    public CustomerOrderNotFoundException(UUID orderId) {
        super("Nie znaleziono zamówienia: " + orderId);
    }
}
