package com.example.manufacturing_order.config;

import com.example.manufacturing_order.adapter.output.persistence.stock.DataMaterialStockRepository;
import com.example.manufacturing_order.adapter.output.persistence.stock.MaterialStockEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MaterialStockDataInitializer implements ApplicationRunner {

    private final DataMaterialStockRepository stockRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (stockRepository.count() > 0) {
            return;
        }
        stockRepository.save(new MaterialStockEntity("METAL", 500));
        stockRepository.save(new MaterialStockEntity("WOOD", 500));
        log.info("Zainicjalizowano stany magazynowe materiałów METAL i WOOD");
    }
}
