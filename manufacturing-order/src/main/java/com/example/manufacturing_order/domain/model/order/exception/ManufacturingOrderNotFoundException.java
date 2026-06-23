package com.example.manufacturing_order.domain.model.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Nie znaleziono zamówienia")
public class ManufacturingOrderNotFoundException extends RuntimeException {
    public ManufacturingOrderNotFoundException(UUID orderId) {
        super("Nie znaleziono zlecenia produkcyjnego dla zamówienia: " + orderId);
    }
}
