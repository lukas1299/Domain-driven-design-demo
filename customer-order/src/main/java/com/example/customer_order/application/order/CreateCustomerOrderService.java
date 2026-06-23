package com.example.customer_order.application.order;

import com.example.customer_order.adapter.output.manufacturing.ProductAvailabilityPort;
import com.example.customer_order.adapter.output.persistence.order.CustomerOrderRepositoryPort;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;
import com.example.customer_order.domain.model.order.exception.ProductNotAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateCustomerOrderService {

    private final CustomerOrderRepositoryPort repositoryPort;
    private final ProductAvailabilityPort productAvailabilityPort;

    @Transactional
    public UUID handle(CreateCustomerOrderCommand command) {
        if (!productAvailabilityPort.isProductAvailable(command.productSku())) {
            throw new ProductNotAvailableException(command.productSku());
        }

        CustomerOrder order = CustomerOrder.createNew(
                String.valueOf(command.customerId()),
                command.productSku(),
                command.quantity()
        );

        repositoryPort.save(order);

        return order.getId().value();
    }
}