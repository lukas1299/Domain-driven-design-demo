package com.example.customer_order.adapter.output.persistence.cancel;

import com.example.customer_order.adapter.output.persistence.order.CustomerOrderEntity;
import com.example.customer_order.adapter.output.persistence.order.CustomerOrderRepository;
import com.example.customer_order.domain.model.order.CustomerOrder;
import com.example.customer_order.domain.model.order.OrderId;
import com.example.customer_order.domain.model.order.OrderStatus;

import com.example.customer_order.domain.model.order.exception.ManufacturingServiceUnavailableException;
import com.example.customer_order.domain.model.order.exception.OrderManufacturingStartedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CancelCustomerOrderAdapter implements CancelCustomerOrderPort{

    private final CustomerOrderRepository customerOrderRepository;
    private final RestClient restClient;

    @Override
    public Optional<CustomerOrder> findById(UUID orderId) {
        return customerOrderRepository.findById(orderId)
                .map(entity -> CustomerOrder.of(
                        OrderId.of(entity.getId()),
                        entity.getCustomerId(),
                        entity.getProductSku(),
                        entity.getQuantity(),
                        OrderStatus.valueOf(entity.getStatus().name())
                ));
    }

    @Override
    public void cancel(CustomerOrder order) {
        if(!order.getStatus().equals(OrderStatus.PLACED)) {
            try {
                restClient.post()
                        .uri("/v1/manufacturing-orders/{orderId}/cancel", order.getCustomerId())
                        .retrieve()
                        .toBodilessEntity();
            } catch (RestClientResponseException exception) {
                if (exception.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) {
                    throw new OrderManufacturingStartedException("Nie można anulować rozpoczętego zamówienia.");
                }
                throw new ManufacturingServiceUnavailableException(exception);
            }
        }
        order.setStatus(OrderStatus.CANCEL);
        CustomerOrderEntity entity = toEntity(order);
        customerOrderRepository.save(entity);
    }

    private CustomerOrderEntity toEntity(CustomerOrder order) {
        CustomerOrderEntity entity = new CustomerOrderEntity();
        entity.setId(order.getId().value());
        entity.setCustomerId(order.getCustomerId());
        entity.setProductSku(order.getProductSku());
        entity.setQuantity(order.getQuantity());
        entity.setStatus(order.getStatus());
        return entity;
    }

}
