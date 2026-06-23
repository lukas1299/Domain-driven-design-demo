package com.example.customer_order.adapter.output.persistence.payment;

import com.example.customer_order.domain.model.order.CustomerOrder;
import java.util.Optional;
import java.util.UUID;

public interface PayCustomerOrderRepositoryPort {
    Optional<CustomerOrder> findById(UUID id);
    void save(CustomerOrder order);
}