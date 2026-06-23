package com.example.customer_order.application.order;

import com.example.customer_order.adapter.output.manufacturing.ProductAvailabilityPort;
import com.example.customer_order.adapter.output.persistence.order.UpdateCustomerOrderRepositoryPort;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;
import com.example.customer_order.domain.model.order.OrderStatus;
import com.example.customer_order.domain.model.order.exception.CustomerOrderNotFoundException;
import com.example.customer_order.domain.model.order.exception.ProductNotAvailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerOrderHandlerTest {

    @Mock
    private UpdateCustomerOrderRepositoryPort repositoryPort;

    @Mock
    private ProductAvailabilityPort productAvailabilityPort;

    @InjectMocks
    private UpdateCustomerOrderHandler handler;

    @Test
    void handle_shouldUpdateOrderWhenProductAvailable() {
        UUID orderId = UUID.randomUUID();
        CustomerOrder order = placedOrder(orderId);
        when(repositoryPort.findById(orderId)).thenReturn(Optional.of(order));
        when(productAvailabilityPort.isProductAvailable("SKU-2")).thenReturn(true);

        CustomerOrder result = handler.handle(new UpdateCustomerOrderCommand(orderId, "SKU-2", 5));

        assertThat(result.getProductSku()).isEqualTo("SKU-2");
        assertThat(result.getQuantity()).isEqualTo(5);
        verify(repositoryPort).save(order);
    }

    @Test
    void handle_shouldThrowWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(repositoryPort.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(new UpdateCustomerOrderCommand(orderId, "SKU-2", 5)))
                .isInstanceOf(CustomerOrderNotFoundException.class);

        verify(repositoryPort, never()).save(any());
    }

    @Test
    void handle_shouldThrowWhenProductNotAvailable() {
        UUID orderId = UUID.randomUUID();
        when(repositoryPort.findById(orderId)).thenReturn(Optional.of(placedOrder(orderId)));
        when(productAvailabilityPort.isProductAvailable("SKU-2")).thenReturn(false);

        assertThatThrownBy(() -> handler.handle(new UpdateCustomerOrderCommand(orderId, "SKU-2", 5)))
                .isInstanceOf(ProductNotAvailableException.class);

        verify(repositoryPort, never()).save(any());
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
