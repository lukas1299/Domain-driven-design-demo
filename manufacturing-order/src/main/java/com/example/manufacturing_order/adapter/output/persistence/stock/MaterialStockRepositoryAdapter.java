package com.example.manufacturing_order.adapter.output.persistence.stock;

import com.example.manufacturing_order.domain.model.stock.MaterialStock;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MaterialStockRepositoryAdapter implements MaterialStockRepositoryPort {
    private final DataMaterialStockRepository repository;

    @Override
    public MaterialStock save(MaterialStock materialStock) {
        MaterialStockEntity entity = repository.findById(materialStock.getMaterialSku())
                .orElseGet(MaterialStockEntity::new);
        entity.setMaterialSku(materialStock.getMaterialSku());
        entity.setQuantity(materialStock.getQuantity());
        repository.save(entity);
        return materialStock;
    }

    @Override
    public List<MaterialStock> findByMaterialSkus(Collection<String> materialSkus) {
        return repository.findByMaterialSkuIn(materialSkus).stream()
                .map(this::toDomain)
                .toList();
    }

    private MaterialStock toDomain(MaterialStockEntity entity) {
        return MaterialStock.of(entity.getMaterialSku(), entity.getQuantity());
    }
}
