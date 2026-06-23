package com.example.customer_order.adapter.input.messaging;

import com.example.common.messaging.order.ManufacturingOrderKafkaTopics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ManufacturingFinishedOrderListener {

    private static final Logger log = LogManager.getLogger(ManufacturingFinishedOrderListener.class);

    @KafkaListener(topics = ManufacturingOrderKafkaTopics.MANUFACTURING_FINISHED, groupId = "customer-order-service-group")
    public void onOrderFinished(String message) {
        try {
            log.info("Odebrano zakończone zamówienie {}", message);
        } catch (Exception e) {
            log.error("Błąd podczas przetwarzania wiadomości z Kafki: {}", message, e);
            throw new RuntimeException(e);
        }
    }
}
