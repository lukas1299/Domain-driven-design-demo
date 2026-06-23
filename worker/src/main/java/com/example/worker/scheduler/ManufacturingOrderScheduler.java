package com.example.worker.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ManufacturingOrderScheduler {

    private static final Logger log = LoggerFactory.getLogger(ManufacturingOrderScheduler.class);
    private final RestClient restClient;

    public ManufacturingOrderScheduler(@Value("${manufacturing.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Scheduled(fixedRate = 2000, initialDelay = 5000)
    public void triggerManufacturingOrderStart() {
        log.info("Wysyłanie żądania POST do StartManufacturingOrderController...");

        try {
            ResponseEntity<Void> response = restClient.post()
                    .uri("/v1/manufacturing-orders/start")
                    .retrieve()
                    .toEntity(Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Zamówienia produkcyjne zostały pomyślnie uruchomione (HTTP {}).", response.getStatusCode());
            } else {
                log.warn("Otrzymano nieoczekiwany kod statusu: {}", response.getStatusCode());
            }

            Thread.sleep(3000);

            ResponseEntity<Void> finishResponse = restClient.post()
                    .uri("/v1/manufacturing-orders/finish")
                    .retrieve()
                    .toEntity(Void.class);

            if (finishResponse.getStatusCode().is2xxSuccessful()) {
                log.info("Zamówienia produkcyjne zostały pomyślnie zakończone (HTTP {}).", finishResponse.getStatusCode());
            } else {
                log.warn("Otrzymano nieoczekiwany kod statusu przy kończeniu: {}", finishResponse.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Błąd podczas wywoływania handlera zamówień: {}", e.getMessage());
        }
    }
}