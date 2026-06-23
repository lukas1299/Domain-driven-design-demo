package com.example.customer_order.application.cancel;

import com.example.customer_order.adapter.output.persistence.cancel.CancelCustomerOrderPort;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;
import com.example.customer_order.domain.model.order.OrderStatus;
import com.example.customer_order.domain.model.order.exception.CustomerOrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelOrderHandlerTest {

    @Mock
    private CancelCustomerOrderPort cancelCustomerOrderPort;

    private CancelOrderService cancelOrderService;

    @BeforeEach
    void setUp() {
        cancelOrderService = new CancelOrderService(cancelCustomerOrderPort);
    }

    @Test
    void handle_shouldCancelOrder() {
        UUID orderId = UUID.randomUUID();
        CustomerOrder order = CustomerOrder.of(
                OrderId.of(orderId),
                UUID.randomUUID().toString(),
                "SKU-1",
                2,
                OrderStatus.PLACED
        );
        when(cancelCustomerOrderPort.findById(orderId)).thenReturn(Optional.of(order));

        cancelOrderService.handle(new CancelOrderCommand(orderId));

        verify(cancelCustomerOrderPort).cancel(order);
    }

    @Test
    void handle_shouldThrowWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(cancelCustomerOrderPort.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cancelOrderService.handle(new CancelOrderCommand(orderId)))
                .isInstanceOf(CustomerOrderNotFoundException.class);
    }
}
