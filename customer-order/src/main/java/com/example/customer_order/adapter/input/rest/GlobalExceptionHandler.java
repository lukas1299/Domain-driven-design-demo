package com.example.customer_order.adapter.input.rest;

import com.example.customer_order.adapter.input.rest.order.CreateCustomerOrderController;
import com.example.customer_order.adapter.input.rest.order.UpdateCustomerOrderController;
import com.example.customer_order.adapter.input.rest.payment.PayCustomerOrderController;
import com.example.customer_order.domain.model.order.exception.ManufacturingServiceUnavailableException;
import com.example.customer_order.domain.model.order.exception.ProductNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        CreateCustomerOrderController.class,
        UpdateCustomerOrderController.class,
        PayCustomerOrderController.class
})
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<String> handleProductNotAvailable(ProductNotAvailableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ManufacturingServiceUnavailableException.class)
    public ResponseEntity<String> handleManufacturingUnavailable(ManufacturingServiceUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exception.getMessage());
    }
}
