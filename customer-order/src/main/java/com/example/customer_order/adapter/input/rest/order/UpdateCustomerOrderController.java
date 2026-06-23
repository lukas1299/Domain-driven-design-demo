package com.example.customer_order.adapter.input.rest.order;

import com.example.customer_order.application.order.UpdateCustomerOrderCommand;
import com.example.customer_order.application.order.UpdateCustomerOrderHandler;
import com.example.customer_order.domain.model.order.CustomerOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class UpdateCustomerOrderController {

    private final UpdateCustomerOrderHandler handler;

    public UpdateCustomerOrderController(UpdateCustomerOrderHandler handler) {
        this.handler = handler;
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<CustomerOrderResponse> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderRequest request) {

        var command = new UpdateCustomerOrderCommand(orderId, request.productSku(), request.quantity());
        CustomerOrder order = handler.handle(command);
        return ResponseEntity.ok(new CustomerOrderResponse(
                order.getId().value().toString(),
                order.getStatus().name()
        ));
    }
}
