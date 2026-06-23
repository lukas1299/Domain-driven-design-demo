package com.example.manufacturing_order.domain.model.stock;

public class InsufficientMaterialStockException extends RuntimeException {
    public InsufficientMaterialStockException(String materialSku, int required, int available) {
        super("Niewystarczający stan magazynowy materiału %s: wymagane %d, dostępne %d"
                .formatted(materialSku, required, available));
    }
}
