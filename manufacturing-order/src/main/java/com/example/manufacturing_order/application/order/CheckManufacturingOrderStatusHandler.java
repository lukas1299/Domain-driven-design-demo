package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.messaging.order.ManufacturingOrderFinishedEventPublisherPort;
import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class CheckManufacturingOrderStatusHandler {

    private final ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;
    private final ManufacturingOrderFinishedEventPublisherPort eventPublisherPort;

    @Transactional
    public void handle() {
        manufacturingOrderRepositoryPort.findFirstFinished()
                .ifPresent(this::publishAndMarkAsNotified);
    }

    private void publishAndMarkAsNotified(ManufacturingOrder order) {
        eventPublisherPort.publish(order);
        order.markAsNotified();
        manufacturingOrderRepositoryPort.save(order);
    }
}
