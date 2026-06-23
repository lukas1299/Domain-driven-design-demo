package com.example.customer_order.adapter.output.outbox;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepository extends CrudRepository<OutboxEntity, UUID> {
    List<OutboxEntity> findByProcessedFalse();
}
