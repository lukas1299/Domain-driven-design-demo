package com.example.customer_order.adapter.output.persistence.order;

import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;

import java.util.Optional;

public interface CustomerOrderRepositoryPort {
    CustomerOrder save(CustomerOrder customerOrder);
    Optional<CustomerOrder> findById(OrderId id);
}