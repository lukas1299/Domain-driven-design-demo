package com.example.customer_order.domain.model.order;

import com.example.customer_order.adapter.output.messaging.payment.CustomerOrderPaidDomainEvent;
import com.example.customer_order.domain.model.order.exception.InvalidOrderStatusException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerOrderTest {

    private static final String CUSTOMER_ID = UUID.randomUUID().toString();

    @Test
    void createNew_shouldCreatePlacedOrder() {
        CustomerOrder order = CustomerOrder.createNew(CUSTOMER_ID, "SKU-1", 3);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(order.getProductSku()).isEqualTo("SKU-1");
        assertThat(order.getQuantity()).isEqualTo(3);
        assertThat(order.getDomainEvents()).isEmpty();
    }

    @Test
    void createNew_shouldRejectNonPositiveQuantity() {
        assertThatThrownBy(() -> CustomerOrder.createNew(CUSTOMER_ID, "SKU-1", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity");
    }

    @Test
    void updateDetails_shouldUpdatePlacedOrder() {
        CustomerOrder order = placedOrder();

        order.updateDetails("SKU-2", 5);

        assertThat(order.getProductSku()).isEqualTo("SKU-2");
        assertThat(order.getQuantity()).isEqualTo(5);
    }

    @Test
    void updateDetails_shouldRejectWhenNotPlaced() {
        CustomerOrder order = paidOrder();

        assertThatThrownBy(() -> order.updateDetails("SKU-2", 5))
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    void ensureDeletable_shouldAllowPlacedOrder() {
        CustomerOrder order = placedOrder();

        order.ensureDeletable();
    }

    @Test
    void ensureDeletable_shouldRejectWhenNotPlaced() {
        CustomerOrder order = paidOrder();

        assertThatThrownBy(order::ensureDeletable)
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    void pay_shouldTransitionToPaidAndEmitEvent() {
        CustomerOrder order = placedOrder();

        order.pay();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(order.getDomainEvents()).hasSize(1);
        assertThat(order.getDomainEvents().get(0)).isInstanceOf(CustomerOrderPaidDomainEvent.class);

        CustomerOrderPaidDomainEvent event = (CustomerOrderPaidDomainEvent) order.getDomainEvents().get(0);
        assertThat(event.orderId()).isEqualTo(order.getId().value());
        assertThat(event.productSku()).isEqualTo("SKU-1");
        assertThat(event.quantity()).isEqualTo(2);
    }

    @Test
    void pay_shouldRejectWhenNotPlaced() {
        CustomerOrder order = paidOrder();

        assertThatThrownBy(order::pay)
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    void markAsManufacturingTriggered_shouldUpdateStatus() {
        CustomerOrder order = paidOrder();

        order.markAsManufacturingTriggered();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.MANUFACTURING_TRIGGERED);
    }

    @Test
    void cancel_shouldUpdateStatus() {
        CustomerOrder order = placedOrder();

        order.cancel();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    void clearDomainEvents_shouldRemoveEvents() {
        CustomerOrder order = placedOrder();
        order.pay();

        order.clearDomainEvents();

        assertThat(order.getDomainEvents()).isEmpty();
    }

    private CustomerOrder placedOrder() {
        return CustomerOrder.of(
                OrderId.generate(),
                CUSTOMER_ID,
                "SKU-1",
                2,
                OrderStatus.PLACED
        );
    }

    private CustomerOrder paidOrder() {
        return CustomerOrder.of(
                OrderId.generate(),
                CUSTOMER_ID,
                "SKU-1",
                2,
                OrderStatus.PAID
        );
    }
}
