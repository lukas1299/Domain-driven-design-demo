package com.example.customer_order.domain.model.order.exception;

public class ManufacturingServiceUnavailableException extends RuntimeException {
    public ManufacturingServiceUnavailableException(Throwable cause) {
        super("Serwis manufacturing jest niedostępny", cause);
    }
}
