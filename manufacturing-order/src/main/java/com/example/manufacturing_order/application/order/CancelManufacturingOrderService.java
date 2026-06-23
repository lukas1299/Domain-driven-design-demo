package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import com.example.manufacturing_order.domain.model.order.exception.ManufacturingOrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
public class CancelManufacturingOrderService {

    private final ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;

    @Transactional
    public void handle(UUID sourceOrderId) {
        ManufacturingOrder order = manufacturingOrderRepositoryPort.findBySourceOrderId(sourceOrderId.toString())
                .orElseThrow(() -> new ManufacturingOrderNotFoundException(sourceOrderId));

        order.cancelProduction();
        manufacturingOrderRepositoryPort.save(order);
    }
}
