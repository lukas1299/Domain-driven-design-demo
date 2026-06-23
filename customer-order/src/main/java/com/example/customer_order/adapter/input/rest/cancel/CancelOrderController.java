package com.example.customer_order.adapter.input.rest.cancel;

import com.example.customer_order.application.cancel.CancelOrderCommand;
import com.example.customer_order.application.cancel.CancelOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CancelOrderController {

    public final CancelOrderService cancelOrderService;

    @PostMapping("/{orderId}/cancel")
    @ResponseStatus(code = HttpStatus.OK)
    public void handleCancel(@PathVariable("orderId") UUID orderId) {
        CancelOrderCommand command = new CancelOrderCommand(orderId);
        cancelOrderService.handle(command);
    }
}
