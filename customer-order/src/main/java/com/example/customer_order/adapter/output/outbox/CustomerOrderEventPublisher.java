package com.example.customer_order.adapter.output.outbox;

import com.example.common.messaging.order.CustomerOrderKafkaTopics;
import com.example.common.messaging.order.OrderCreatedEventPayload;
import com.example.customer_order.domain.model.order.CustomerOrder;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomerOrderEventPublisher implements ManufacturingEventPublisherPort {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void publishOrderPaid(CustomerOrder order) {
        try {
            OrderCreatedEventPayload payload = new OrderCreatedEventPayload(
                    order.getId().value().toString(),
                    order.getProductSku(),
                    order.getQuantity()
            );

            String jsonPayload = objectMapper.writeValueAsString(payload);

            OutboxEntity outboxEntry = new OutboxEntity(
                    order.getId().value().toString(),
                    CustomerOrderKafkaTopics.ORDER_PAID,
                    jsonPayload
            );

            outboxRepository.save(outboxEntry);

        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas zapisu do Outboxa", e);
        }
    }
}
