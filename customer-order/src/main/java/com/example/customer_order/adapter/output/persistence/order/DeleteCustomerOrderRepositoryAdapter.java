package com.example.customer_order.adapter.output.persistence.order;

import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;
import com.example.customer_order.domain.model.order.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DeleteCustomerOrderRepositoryAdapter implements DeleteCustomerOrderRepositoryPort {

    private final CustomerOrderRepository customerOrderRepository;

    public DeleteCustomerOrderRepositoryAdapter(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    @Override
    public Optional<CustomerOrder> findById(UUID id) {
        return customerOrderRepository.findById(id)
                .map(entity -> CustomerOrder.of(
                        OrderId.of(entity.getId()),
                        entity.getCustomerId(),
                        entity.getProductSku(),
                        entity.getQuantity(),
                        OrderStatus.valueOf(entity.getStatus().name())
                ));
    }

    @Override
    public void delete(CustomerOrder order) {
        customerOrderRepository.deleteById(order.getId().value());
    }
}
