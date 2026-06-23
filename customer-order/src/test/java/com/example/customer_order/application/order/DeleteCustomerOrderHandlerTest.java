package com.example.customer_order.application.order;

import com.example.customer_order.adapter.output.persistence.order.DeleteCustomerOrderRepositoryPort;
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
class DeleteCustomerOrderHandlerTest {

    @Mock
    private DeleteCustomerOrderRepositoryPort repositoryPort;

    @InjectMocks
    private DeleteCustomerOrderHandler handler;

    @Test
    void handle_shouldDeletePlacedOrder() {
        UUID orderId = UUID.randomUUID();
        CustomerOrder order = placedOrder(orderId);
        when(repositoryPort.findById(orderId)).thenReturn(Optional.of(order));

        handler.handle(new DeleteCustomerOrderCommand(orderId));

        verify(repositoryPort).delete(order);
    }

    @Test
    void handle_shouldThrowWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(repositoryPort.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(new DeleteCustomerOrderCommand(orderId)))
                .isInstanceOf(CustomerOrderNotFoundException.class);

        verify(repositoryPort, never()).delete(any());
    }

    @Test
    void handle_shouldThrowWhenOrderNotDeletable() {
        UUID orderId = UUID.randomUUID();
        CustomerOrder order = CustomerOrder.of(
                OrderId.of(orderId),
                UUID.randomUUID().toString(),
                "SKU-1",
                2,
                OrderStatus.PAID
        );
        when(repositoryPort.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> handler.handle(new DeleteCustomerOrderCommand(orderId)))
                .isInstanceOf(InvalidOrderStatusException.class);

        verify(repositoryPort, never()).delete(any());
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
