package com.example.manufacturing_order.domain.model.billOfMaterials;

public class BillOfMaterialsNotFoundException extends RuntimeException {
    public BillOfMaterialsNotFoundException(String productSku) {
        super("Nie znaleziono receptury dla produktu: " + productSku);
    }
}
