package com.example.manufacturing_order.adapter.input.rest.product;

import com.example.common.api.product.ProductAvailabilityResponse;
import com.example.manufacturing_order.application.product.CheckProductAvailabilityPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
public class ProductAvailabilityController {

    private final CheckProductAvailabilityPort checkProductAvailabilityPort;

    public ProductAvailabilityController(CheckProductAvailabilityPort checkProductAvailabilityPort) {
        this.checkProductAvailabilityPort = checkProductAvailabilityPort;
    }

    @GetMapping("/{productSku}/availability")
    public ProductAvailabilityResponse checkAvailability(@PathVariable String productSku) {
        return checkProductAvailabilityPort.checkSubProductsAvailability(productSku);
    }
}
