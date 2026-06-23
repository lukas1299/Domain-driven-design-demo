package com.example.manufacturing_order.application.product;

import com.example.common.api.product.ProductAvailabilityResponse;
import com.example.manufacturing_order.adapter.output.persistence.bom.BillOfMaterialsRepositoryPort;
import com.example.manufacturing_order.adapter.output.persistence.stock.MaterialStockRepositoryPort;
import com.example.manufacturing_order.domain.model.billOfMaterials.BillOfMaterials;
import com.example.manufacturing_order.domain.model.order.MaterialRequirement;
import com.example.manufacturing_order.domain.model.stock.MaterialStock;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CheckProductAvailabilityHandler implements CheckProductAvailabilityPort {

    private final BillOfMaterialsRepositoryPort billOfMaterialsRepositoryPort;
    private final MaterialStockRepositoryPort materialStockRepositoryPort;

    @Override
    public ProductAvailabilityResponse checkSubProductsAvailability(String productSku) {
        return billOfMaterialsRepositoryPort.findByProductSku(productSku)
                .map(bom -> new ProductAvailabilityResponse(productSku, hasSufficientStock(bom, 1)))
                .orElseGet(() -> new ProductAvailabilityResponse(productSku, false));
    }

    private boolean hasSufficientStock(BillOfMaterials billOfMaterials, int quantity) {
        List<MaterialRequirement> requirements = billOfMaterials.calculateRequirements(quantity);
        List<String> materialSkus = requirements.stream()
                .map(MaterialRequirement::semiProductSku)
                .toList();

        Map<String, MaterialStock> stocksBySku = materialStockRepositoryPort.findByMaterialSkus(materialSkus).stream()
                .collect(Collectors.toMap(MaterialStock::getMaterialSku, Function.identity()));

        return requirements.stream().allMatch(materialRequirement -> {
            MaterialStock materialStock = stocksBySku.get(materialRequirement.semiProductSku());
            return hasEnoughStockForRequirements(materialStock, materialRequirement);
        });
    }

    private boolean hasEnoughStockForRequirements(MaterialStock materialStock, MaterialRequirement materialRequirement) {
        return materialStock != null && materialStock.hasEnough(materialRequirement.requiredQuantity());
    }
}
