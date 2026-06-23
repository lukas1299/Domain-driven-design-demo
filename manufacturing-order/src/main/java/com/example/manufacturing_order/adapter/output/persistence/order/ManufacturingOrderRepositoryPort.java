package com.example.manufacturing_order.adapter.output.persistence.order;

import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrderId;

import java.util.Optional;

public interface ManufacturingOrderRepositoryPort {
    ManufacturingOrder save(ManufacturingOrder manufacturingOrder);
    Optional<ManufacturingOrder> findById(ManufacturingOrderId id);
    Optional<ManufacturingOrder> findBySourceOrderId(String sourceOrderId);
    Optional<ManufacturingOrder> findFirstPending();
    Optional<ManufacturingOrder> findFirstInProgress();
    Optional<ManufacturingOrder> findFirstFinished();
}
