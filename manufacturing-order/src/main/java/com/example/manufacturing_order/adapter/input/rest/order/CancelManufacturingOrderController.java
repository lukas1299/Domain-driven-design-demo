package com.example.manufacturing_order.adapter.input.rest.order;

import com.example.manufacturing_order.application.order.CancelManufacturingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/manufacturing-orders")
@RequiredArgsConstructor
public class CancelManufacturingOrderController {

    private final CancelManufacturingOrderService service;

    @PostMapping("/{orderId}/cancel")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelProduction(@PathVariable("orderId") UUID orderId) {
        service.handle(orderId);
    }
}
