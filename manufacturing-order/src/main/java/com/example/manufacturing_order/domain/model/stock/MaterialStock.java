package com.example.manufacturing_order.domain.model.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MaterialStock {
    private final String materialSku;
    private int quantity;

    public static MaterialStock createNew(String materialSku, int initialQuantity) {
        if (materialSku == null || materialSku.isBlank()) {
            throw new IllegalArgumentException("SKU materiału musi być określone.");
        }
        if (initialQuantity < 0) {
            throw new IllegalArgumentException("Stan magazynowy nie może być ujemny.");
        }
        return new MaterialStock(materialSku, initialQuantity);
    }

    public static MaterialStock of(String materialSku, int quantity) {
        return new MaterialStock(materialSku, quantity);
    }

    public void consume(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Ilość do pobrania musi być większa od zera.");
        }
        if (quantity < amount) {
            throw new InsufficientMaterialStockException(materialSku, amount, quantity);
        }
        quantity -= amount;
    }

    public boolean hasEnough(int amount) {
        return amount > 0 && quantity >= amount;
    }

}
