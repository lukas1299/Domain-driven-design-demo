package com.example.customer_order.adapter.output.outbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxProcessor(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 1000)
    public void processOutboxMessages() {
        List<OutboxEntity> pendingMessages = outboxRepository.findByProcessedFalse();

        for (OutboxEntity message : pendingMessages) {
            try {
                kafkaTemplate.send(message.getTopic(), message.getAggregateId(), message.getPayload()).get();
                message.setProcessed(true);
                outboxRepository.save(message);
                log.info("Wysłano wiadomość z Outbox na topic {} (aggregateId={})", message.getTopic(), message.getAggregateId());
            } catch (Exception e) {
                log.error("Nie udało się wysłać wiadomości z Outbox: {}", message.getId(), e);
            }
        }
    }
}
