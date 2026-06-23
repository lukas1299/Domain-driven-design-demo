package com.example.manufacturing_order.config;

import com.example.manufacturing_order.adapter.output.persistence.bom.ProductBomLineEntity;
import com.example.manufacturing_order.adapter.output.persistence.bom.SpringDataProductBomLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BomDataInitializer implements ApplicationRunner {
    private final SpringDataProductBomLineRepository bomLineRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (bomLineRepository.count() > 0) {
            return;
        }

        bomLineRepository.save(new ProductBomLineEntity(UUID.randomUUID(), "CHAIR-01", "METAL", 10));
        bomLineRepository.save(new ProductBomLineEntity(UUID.randomUUID(), "CHAIR-01", "WOOD", 15));
        bomLineRepository.save(new ProductBomLineEntity(UUID.randomUUID(), "TABLE-01", "METAL", 20));
        bomLineRepository.save(new ProductBomLineEntity(UUID.randomUUID(), "TABLE-01", "WOOD", 30));

        log.info("Zainicjalizowano dane BOM dla produktów CHAIR-01 i TABLE-01");
    }
}
