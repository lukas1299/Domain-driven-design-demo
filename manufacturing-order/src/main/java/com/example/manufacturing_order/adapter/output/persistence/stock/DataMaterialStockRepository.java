package com.example.manufacturing_order.adapter.output.persistence.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DataMaterialStockRepository extends JpaRepository<MaterialStockEntity, String> {
    List<MaterialStockEntity> findByMaterialSkuIn(Collection<String> materialSkus);
}
