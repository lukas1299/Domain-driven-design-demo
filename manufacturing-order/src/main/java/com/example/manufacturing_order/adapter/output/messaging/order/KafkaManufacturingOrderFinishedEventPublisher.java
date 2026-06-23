package com.example.manufacturing_order.adapter.output.messaging.order;

import com.example.common.messaging.order.ManufacturingOrderFinishedEventPayload;
import com.example.common.messaging.order.ManufacturingOrderKafkaTopics;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public class KafkaManufacturingOrderFinishedEventPublisher implements ManufacturingOrderFinishedEventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaManufacturingOrderFinishedEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(ManufacturingOrder order) {
        try {
            ManufacturingOrderFinishedEventPayload payload = new ManufacturingOrderFinishedEventPayload(
                    order.getId().value().toString(),
                    order.getSourceOrderId(),
                    order.getProductSku(),
                    order.getQuantity(),
                    order.getStatus().name()
            );

            String jsonPayload = objectMapper.writeValueAsString(payload);

            kafkaTemplate.send(
                    ManufacturingOrderKafkaTopics.MANUFACTURING_FINISHED,
                    order.getSourceOrderId(),
                    jsonPayload
            ).get();
            log.info("Wysłano event zakończenia produkcji: manufacturingOrderId={}, sourceOrderId={}",
                    order.getId().value(), order.getSourceOrderId());
        } catch (Exception exception) {
            throw new RuntimeException("Błąd podczas wysyłania eventu zakończenia produkcji", exception);
        }
    }
}
