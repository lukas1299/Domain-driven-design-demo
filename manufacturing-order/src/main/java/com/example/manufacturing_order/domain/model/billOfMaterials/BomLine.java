package com.example.manufacturing_order.domain.model.billOfMaterials;

import org.apache.commons.lang3.StringUtils;

public record BomLine(String semiProductSku, int quantityPerUnit) {
    public BomLine {
        if (StringUtils.isBlank(semiProductSku)) {
            throw new IllegalArgumentException("SKU półproduktu musi być określone.");
        }
        if (quantityPerUnit <= 0) {
            throw new IllegalArgumentException("Ilość na sztukę musi być większa od zera.");
        }
    }
}
