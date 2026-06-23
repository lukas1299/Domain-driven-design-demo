package com.example.manufacturing_order.adapter.input.scheduler;

import com.example.manufacturing_order.application.order.CheckManufacturingOrderStatusHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class ManufacturingOrderScheduler {
    private final CheckManufacturingOrderStatusHandler checkManufacturingOrderStatusHandler;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void processFinishedManufacturingOrders() {
        try {
            checkManufacturingOrderStatusHandler.handle();
        } catch (Exception exception) {
            log.error("Błąd podczas przetwarzania zakończonych zleceń produkcyjnych: {}", exception.getMessage());
        }
    }
}
