package com.example.customer_order.adapter.input.rest.order;

import com.example.customer_order.application.order.DeleteCustomerOrderCommand;
import com.example.customer_order.application.order.DeleteCustomerOrderHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class DeleteCustomerOrderController {

    private final DeleteCustomerOrderHandler handler;

    @DeleteMapping("/{orderId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable UUID orderId) {
        handler.handle(new DeleteCustomerOrderCommand(orderId));
    }
}
