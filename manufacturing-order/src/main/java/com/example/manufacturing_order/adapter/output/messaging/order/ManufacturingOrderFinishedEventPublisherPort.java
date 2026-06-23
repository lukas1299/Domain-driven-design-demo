package com.example.manufacturing_order.adapter.output.messaging.order;

import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;

public interface ManufacturingOrderFinishedEventPublisherPort {
    void publish(ManufacturingOrder order);
}
