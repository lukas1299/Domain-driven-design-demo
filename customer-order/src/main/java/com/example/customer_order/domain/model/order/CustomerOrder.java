package com.example.customer_order.domain.model.order;

import com.example.customer_order.adapter.output.messaging.payment.CustomerOrderPaidDomainEvent;
import com.example.customer_order.domain.model.order.exception.InvalidOrderStatusException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CustomerOrder {
    private final OrderId id;
    private final String customerId;
    private String productSku;
    private int quantity;
    private OrderStatus status;

    private final List<Object> domainEvents = new ArrayList<>();

    public static CustomerOrder createNew(String customerId, String productSku, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");
        return new CustomerOrder(OrderId.generate(), customerId, productSku, quantity, OrderStatus.PLACED);
    }

    public static CustomerOrder of(OrderId orderId, String customerId, String productSku, int quantity, OrderStatus status) {
        return new CustomerOrder(orderId, customerId, productSku, quantity, status);
    }

    public void updateDetails(String productSku, int quantity) {
        if (!OrderStatus.PLACED.equals(this.status)) {
            throw new InvalidOrderStatusException("Nie można edytować zamówienia o statusie: " + this.status);
        }
        if (productSku == null || productSku.isBlank()) {
            throw new IllegalArgumentException("Product SKU must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        this.productSku = productSku;
        this.quantity = quantity;
    }

    public void ensureDeletable() {
        if (!OrderStatus.PLACED.equals(this.status)) {
            throw new InvalidOrderStatusException("Nie można usunąć zamówienia o statusie: " + this.status);
        }
    }

    public void pay() {
        if (!OrderStatus.PLACED.equals(this.status)) {
            throw new InvalidOrderStatusException("Nie można opłacić zamówienia o statusie: " + this.status);
        }

        this.status = OrderStatus.PAID;

        this.domainEvents.add(new CustomerOrderPaidDomainEvent(
                this.id.value(), UUID.fromString(this.customerId), this.productSku, this.quantity
        ));
    }

    public void markAsManufacturingTriggered() {
        this.status = OrderStatus.MANUFACTURING_TRIGGERED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCEL;
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}