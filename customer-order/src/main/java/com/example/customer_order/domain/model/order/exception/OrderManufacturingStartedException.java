package com.example.customer_order.domain.model.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Nie można anulować rozpoczętego zamówienia.")
public class OrderManufacturingStartedException extends RuntimeException {
    public OrderManufacturingStartedException(String message) {
        super(message);
    }
}
