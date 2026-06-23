package com.example.customer_order.adapter.output.outbox;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_messages")
public class OutboxEntity {

    @Id
    private UUID id;
    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;
    @Column(name = "topic", nullable = false)
    private String topic;
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;
    @Column(name = "processed", nullable = false)
    private boolean processed;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected OutboxEntity() {}

    public OutboxEntity(String aggregateId, String topic, String payload) {
        this.id = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.topic = topic;
        this.payload = payload;
        this.processed = false;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getAggregateId() { return aggregateId; }
    public String getTopic() { return topic; }
    public String getPayload() { return payload; }

    public boolean isProcessed() { return processed; }
    public void setProcessed(boolean processed) { this.processed = processed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}