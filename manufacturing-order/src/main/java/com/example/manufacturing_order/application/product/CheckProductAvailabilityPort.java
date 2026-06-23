package com.example.manufacturing_order.application.product;

import com.example.common.api.product.ProductAvailabilityResponse;

public interface CheckProductAvailabilityPort {
    ProductAvailabilityResponse checkSubProductsAvailability(String productSku);
}
