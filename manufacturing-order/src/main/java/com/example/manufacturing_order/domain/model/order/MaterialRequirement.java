package com.example.manufacturing_order.domain.model.order;

public record MaterialRequirement(String semiProductSku, int requiredQuantity) {
    public MaterialRequirement {
        if (semiProductSku == null || semiProductSku.isBlank()) {
            throw new IllegalArgumentException("SKU półproduktu musi być określone.");
        }
        if (requiredQuantity <= 0) {
            throw new IllegalArgumentException("Wymagana ilość musi być większa od zera.");
        }
    }
}
