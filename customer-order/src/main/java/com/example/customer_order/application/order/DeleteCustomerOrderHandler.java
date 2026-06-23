package com.example.customer_order.application.order;

import com.example.customer_order.adapter.output.persistence.order.DeleteCustomerOrderRepositoryPort;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.exception.CustomerOrderNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class DeleteCustomerOrderHandler {

    private final DeleteCustomerOrderRepositoryPort repositoryPort;

    public DeleteCustomerOrderHandler(DeleteCustomerOrderRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Transactional
    public void handle(DeleteCustomerOrderCommand command) {
        CustomerOrder order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> new CustomerOrderNotFoundException(command.orderId()));

        order.ensureDeletable();

        repositoryPort.delete(order);
    }
}
