package com.example.customer_order.adapter.output.persistence.order;

import com.example.customer_order.domain.model.order.CustomerOrder;

import java.util.Optional;
import java.util.UUID;

public interface DeleteCustomerOrderRepositoryPort {
    Optional<CustomerOrder> findById(UUID id);
    void delete(CustomerOrder order);
}
