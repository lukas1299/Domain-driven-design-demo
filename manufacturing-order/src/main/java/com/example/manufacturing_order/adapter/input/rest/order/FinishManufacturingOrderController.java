package com.example.manufacturing_order.adapter.input.rest.order;

import com.example.manufacturing_order.application.order.FinishManufacturingOrderHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/manufacturing-orders")
public class FinishManufacturingOrderController {

    private final FinishManufacturingOrderHandler handler;

    public FinishManufacturingOrderController(FinishManufacturingOrderHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/finish")
    public ResponseEntity<Void> finishProduction() {
        handler.handle();
        return ResponseEntity.noContent().build();
    }
}
