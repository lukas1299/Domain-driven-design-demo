package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.adapter.output.persistence.stock.MaterialStockRepositoryPort;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import com.example.manufacturing_order.domain.model.order.MaterialRequirement;
import com.example.manufacturing_order.domain.model.stock.InsufficientMaterialStockException;
import com.example.manufacturing_order.domain.model.stock.MaterialStock;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StartManufacturingOrderService {
    private final ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;
    private final MaterialStockRepositoryPort materialStockRepositoryPort;

    @Transactional
    public void handle() {
        ManufacturingOrder order = manufacturingOrderRepositoryPort.findFirstPending()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono zlecenia produkcyjnego ze statusem PENDING"));

        List<String> materialSkus = order.getMaterialRequirements().stream()
                .map(MaterialRequirement::semiProductSku)
                .toList();

        Map<String, MaterialStock> stocksBySku = materialStockRepositoryPort.findByMaterialSkus(materialSkus).stream()
                .collect(Collectors.toMap(MaterialStock::getMaterialSku, Function.identity()));

        for (MaterialRequirement requirement : order.getMaterialRequirements()) {
            MaterialStock stock = stocksBySku.get(requirement.semiProductSku());
            if (stock == null) {
                throw new InsufficientMaterialStockException(
                        requirement.semiProductSku(), requirement.requiredQuantity(), 0);
            }
            stock.consume(requirement.requiredQuantity());
            materialStockRepositoryPort.save(stock);
        }

        order.startProduction();
        manufacturingOrderRepositoryPort.save(order);
    }
}
