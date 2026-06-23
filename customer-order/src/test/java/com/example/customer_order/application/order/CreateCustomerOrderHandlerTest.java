package com.example.customer_order.application.order;

import com.example.customer_order.adapter.output.manufacturing.ProductAvailabilityPort;
import com.example.customer_order.adapter.output.persistence.order.CustomerOrderRepositoryPort;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.exception.ProductNotAvailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerOrderHandlerTest {

    @Mock
    private CustomerOrderRepositoryPort repositoryPort;

    @Mock
    private ProductAvailabilityPort productAvailabilityPort;

    @InjectMocks
    private CreateCustomerOrderService service;

    @Test
    void handle_shouldCreateOrderWhenProductAvailable() {
        UUID customerId = UUID.randomUUID();
        when(productAvailabilityPort.isProductAvailable("SKU-1")).thenReturn(true);
        UUID orderId = service.handle(new CreateCustomerOrderCommand(customerId.toString(), "SKU-1", 3));
        ArgumentCaptor<CustomerOrder> captor = ArgumentCaptor.forClass(CustomerOrder.class);
        verify(repositoryPort).save(captor.capture());
        assertThat(captor.getValue().getId().value()).isEqualTo(orderId);
        assertThat(captor.getValue().getProductSku()).isEqualTo("SKU-1");
        assertThat(captor.getValue().getQuantity()).isEqualTo(3);
    }

    @Test
    void handle_shouldThrowWhenProductNotAvailable() {
        when(productAvailabilityPort.isProductAvailable("SKU-1")).thenReturn(false);
        assertThatThrownBy(() -> service.handle(new CreateCustomerOrderCommand(UUID.randomUUID().toString(), "SKU-1", 3)))
                .isInstanceOf(ProductNotAvailableException.class);
        verify(repositoryPort, never()).save(any());
    }
}
