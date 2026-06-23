package com.example.customer_order.application.payment;

import com.example.customer_order.adapter.output.outbox.CustomerOrderEventPublisher;
import com.example.customer_order.adapter.output.persistence.payment.PayCustomerOrderRepositoryPort;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;
import com.example.customer_order.domain.model.order.OrderStatus;
import com.example.customer_order.domain.model.order.exception.CustomerOrderNotFoundException;
import com.example.customer_order.domain.model.order.exception.InvalidOrderStatusException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayCustomerOrderHandlerTest {

    @Mock
    private PayCustomerOrderRepositoryPort repositoryPort;

    @Mock
    private CustomerOrderEventPublisher publisher;

    @InjectMocks
    private PayCustomerOrderService service;

    @Test
    void handle_shouldPayOrderAndPublishEvent() {
        UUID orderId = UUID.randomUUID();
        CustomerOrder order = placedOrder(orderId);
        when(repositoryPort.findById(orderId)).thenReturn(Optional.of(order));

        service.handle(new PayCustomerOrderCommand(orderId));

        verify(repositoryPort).save(order);
        verify(publisher).publishOrderPaid(order);
    }

    @Test
    void handle_shouldThrowWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(repositoryPort.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(new PayCustomerOrderCommand(orderId)))
                .isInstanceOf(CustomerOrderNotFoundException.class);

        verify(repositoryPort, never()).save(any());
        verify(publisher, never()).publishOrderPaid(any());
    }

    @Test
    void handle_shouldThrowWhenOrderAlreadyPaid() {
        UUID orderId = UUID.randomUUID();
        CustomerOrder order = CustomerOrder.of(
                OrderId.of(orderId),
                UUID.randomUUID().toString(),
                "SKU-1",
                2,
                OrderStatus.PAID
        );
        when(repositoryPort.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.handle(new PayCustomerOrderCommand(orderId)))
                .isInstanceOf(InvalidOrderStatusException.class);

        verify(repositoryPort, never()).save(any());
        verify(publisher, never()).publishOrderPaid(any());
    }

    private CustomerOrder placedOrder(UUID orderId) {
        return CustomerOrder.of(
                OrderId.of(orderId),
                UUID.randomUUID().toString(),
                "SKU-1",
                2,
                OrderStatus.PLACED
        );
    }
}
