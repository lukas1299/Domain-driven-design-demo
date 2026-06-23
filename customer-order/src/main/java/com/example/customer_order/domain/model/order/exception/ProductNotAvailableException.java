package com.example.customer_order.domain.model.order.exception;

public class ProductNotAvailableException extends RuntimeException {
    public ProductNotAvailableException(String productSku) {
        super("Produkt niedostępny w manufacturing: " + productSku);
    }
}
