package com.example.customer_order.adapter.output.persistence.cancel;

import com.example.customer_order.domain.model.order.CustomerOrder;

import java.util.Optional;
import java.util.UUID;

public interface CancelCustomerOrderPort {
    Optional<CustomerOrder> findById(UUID orderId);
    void cancel(CustomerOrder order);
}
