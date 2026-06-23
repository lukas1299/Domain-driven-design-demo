package com.example.customer_order.adapter.output.outbox;

import com.example.customer_order.domain.model.order.CustomerOrder;

public interface ManufacturingEventPublisherPort {
    void publishOrderPaid(CustomerOrder order);
}