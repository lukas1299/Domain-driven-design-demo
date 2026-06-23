package com.example.manufacturing_order.adapter.output.persistence.bom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataProductBomLineRepository extends JpaRepository<ProductBomLineEntity, UUID> {
    List<ProductBomLineEntity> findByProductSku(String productSku);
    long count();
}
