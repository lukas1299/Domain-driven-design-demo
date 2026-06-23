package com.example.manufacturing_order.adapter.output.persistence.stock;

import com.example.manufacturing_order.domain.model.stock.MaterialStock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MaterialStockRepositoryPort {
    MaterialStock save(MaterialStock materialStock);
    List<MaterialStock> findByMaterialSkus(Collection<String> materialSkus);
}
