package com.example.customer_order.adapter.output.persistence.payment;

import com.example.customer_order.adapter.output.persistence.order.CustomerOrderEntity;
import com.example.customer_order.adapter.output.persistence.order.CustomerOrderRepository;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;
import com.example.customer_order.domain.model.order.OrderStatus;

import com.example.customer_order.domain.model.order.exception.CustomerOrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PayCustomerOrderRepositoryAdapter implements PayCustomerOrderRepositoryPort {

    private final CustomerOrderRepository customerOrderRepository;

    @Override
    public Optional<CustomerOrder> findById(UUID id) {
        return customerOrderRepository.findById(id)
                .map(entity -> {
                    OrderId orderId = OrderId.of(entity.getId());
                    OrderStatus status = OrderStatus.valueOf(entity.getStatus().name());

                    return CustomerOrder.of(
                            orderId,
                            entity.getCustomerId(),
                            entity.getProductSku(),
                            entity.getQuantity(),
                            status
                    );
                });
    }

    @Override
    public void save(CustomerOrder order) {
        CustomerOrderEntity entity = customerOrderRepository.findById(order.getId().value())
                .orElseThrow(() -> new CustomerOrderNotFoundException(order.getId().value()));
        entity.setStatus(order.getStatus());
        customerOrderRepository.save(entity);
    }
}