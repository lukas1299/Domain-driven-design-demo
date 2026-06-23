package com.example.customer_order.application.payment;

import com.example.customer_order.adapter.output.outbox.CustomerOrderEventPublisher;
import com.example.customer_order.adapter.output.persistence.payment.PayCustomerOrderRepositoryPort;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.exception.CustomerOrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PayCustomerOrderService {

    private final PayCustomerOrderRepositoryPort repositoryPort;
    private final CustomerOrderEventPublisher publisher;

    @Transactional
    public void handle(PayCustomerOrderCommand command) {
        CustomerOrder order = repositoryPort.findById(command.orderId())
                .orElseThrow(() -> new CustomerOrderNotFoundException(command.orderId()));
        order.pay();
        repositoryPort.save(order);
        publisher.publishOrderPaid(order);
    }
}