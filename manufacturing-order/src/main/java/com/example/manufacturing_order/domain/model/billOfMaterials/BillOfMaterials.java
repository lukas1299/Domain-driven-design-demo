package com.example.manufacturing_order.domain.model.billOfMaterials;

import com.example.manufacturing_order.domain.model.order.MaterialRequirement;

import java.util.List;

public class BillOfMaterials {
    private final String productSku;
    private final List<BomLine> lines;

    public BillOfMaterials(String productSku, List<BomLine> lines) {
        if (productSku == null || productSku.isBlank()) {
            throw new IllegalArgumentException("SKU produktu musi być określone.");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Receptura musi zawierać co najmniej jedną linię.");
        }
        this.productSku = productSku;
        this.lines = List.copyOf(lines);
    }

    public List<MaterialRequirement> calculateRequirements(int orderQuantity) {
        if (orderQuantity <= 0) {
            throw new IllegalArgumentException("Ilość zamówienia musi być większa od zera.");
        }
        return lines.stream()
                .map(line -> new MaterialRequirement(line.semiProductSku(), line.quantityPerUnit() * orderQuantity))
                .toList();
    }

    public String getProductSku() {
        return productSku;
    }

    public List<BomLine> getLines() {
        return lines;
    }
}
