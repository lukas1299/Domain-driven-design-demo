package com.example.customer_order.adapter.input.rest.order;

import com.example.customer_order.application.order.CreateCustomerOrderCommand;
import com.example.customer_order.application.order.CreateCustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CreateCustomerOrderController {

    private final CreateCustomerOrderService service;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request) {
        var command = new CreateCustomerOrderCommand(
                request.customerId().toString(), request.productSku(), request.quantity()
        );
        UUID orderId = service.handle(command);
        String responseBody = "Zamówienie zarejestrowane. ID: " + orderId + ". Oczekiwanie na płatność.";
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }
}