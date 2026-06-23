package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import org.springframework.transaction.annotation.Transactional;

public class FinishManufacturingOrderHandler {

    private final ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;

    public FinishManufacturingOrderHandler(ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort) {
        this.manufacturingOrderRepositoryPort = manufacturingOrderRepositoryPort;
    }

    @Transactional
    public void handle() {
        ManufacturingOrder order = manufacturingOrderRepositoryPort.findFirstInProgress()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono zlecenia produkcyjnego ze statusem IN_PROGRESS"));

        order.finishProduction();
        manufacturingOrderRepositoryPort.save(order);
    }
}
