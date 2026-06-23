package com.example.manufacturing_order.adapter.input.rest.order;

import com.example.manufacturing_order.application.order.StartManufacturingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/manufacturing-orders")
@RequiredArgsConstructor
public class StartManufacturingOrderController {

    private final StartManufacturingOrderService service;

    @PostMapping("/start")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void startProduction() {
        service.handle();
    }
}
