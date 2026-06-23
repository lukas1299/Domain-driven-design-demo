package com.example.manufacturing_order.adapter.output.persistence.order;

import com.example.manufacturing_order.domain.model.order.ManufacturingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataManufacturingOrderRepository extends JpaRepository<ManufacturingOrderEntity, UUID> {
    Optional<ManufacturingOrderEntity> findBySourceOrderId(String sourceOrderId);
    Optional<ManufacturingOrderEntity> findFirstByStatusOrderByIdAsc(ManufacturingStatus status);
}
