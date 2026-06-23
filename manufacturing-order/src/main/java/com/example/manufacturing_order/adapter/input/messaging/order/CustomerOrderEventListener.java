package com.example.manufacturing_order.adapter.input.messaging.order;

import com.example.common.messaging.order.CustomerOrderKafkaTopics;
import com.example.common.messaging.order.OrderCreatedEventPayload;
import com.example.manufacturing_order.application.order.CreateManufacturingOrderCommand;
import com.example.manufacturing_order.application.order.CreateManufacturingOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerOrderEventListener {

    private final CreateManufacturingOrderService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = CustomerOrderKafkaTopics.ORDER_PAID, groupId = "manufacturing-service-group")
    public void onCustomerOrderPaid(String message) {
        try {
            OrderCreatedEventPayload event = objectMapper.readValue(message, OrderCreatedEventPayload.class);

            log.info("Odebrano opłacone zamówienie klienta: orderId={}, sku={}, qty={}",
                    event.orderId(), event.sku(), event.qty());

            CreateManufacturingOrderCommand command = new CreateManufacturingOrderCommand(
                    event.orderId(),
                    event.sku(),
                    event.qty()
            );

            service.handle(command);
        } catch (Exception e) {
            log.error("Błąd podczas przetwarzania wiadomości z Kafki: {}", message, e);
            throw new RuntimeException(e);
        }
    }
}
