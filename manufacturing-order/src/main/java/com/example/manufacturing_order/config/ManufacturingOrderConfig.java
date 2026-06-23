package com.example.manufacturing_order.config;

import com.example.manufacturing_order.adapter.output.messaging.order.KafkaManufacturingOrderFinishedEventPublisher;
import com.example.manufacturing_order.adapter.output.messaging.order.ManufacturingOrderFinishedEventPublisherPort;
import com.example.manufacturing_order.adapter.output.persistence.bom.BillOfMaterialsRepositoryAdapter;
import com.example.manufacturing_order.adapter.output.persistence.bom.BillOfMaterialsRepositoryPort;
import com.example.manufacturing_order.adapter.output.persistence.bom.SpringDataProductBomLineRepository;
import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryAdapter;
import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.adapter.output.persistence.order.SpringDataManufacturingOrderRepository;
import com.example.manufacturing_order.adapter.output.persistence.stock.DataMaterialStockRepository;
import com.example.manufacturing_order.adapter.output.persistence.stock.MaterialStockRepositoryAdapter;
import com.example.manufacturing_order.adapter.output.persistence.stock.MaterialStockRepositoryPort;
import com.example.manufacturing_order.application.order.*;
import com.example.manufacturing_order.application.product.CheckProductAvailabilityHandler;
import com.example.manufacturing_order.application.product.CheckProductAvailabilityPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableScheduling
public class ManufacturingOrderConfig {

    @Bean
    public ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort(SpringDataManufacturingOrderRepository springDataRepository) {
        return new ManufacturingOrderRepositoryAdapter(springDataRepository);
    }

    @Bean
    public BillOfMaterialsRepositoryPort billOfMaterialsRepositoryPort(SpringDataProductBomLineRepository bomLineRepository) {
        return new BillOfMaterialsRepositoryAdapter(bomLineRepository);
    }

    @Bean
    public CreateManufacturingOrderService createManufacturingOrderService(
            ManufacturingOrderRepositoryPort repositoryPort,
            BillOfMaterialsRepositoryPort billOfMaterialsRepositoryPort
    ) {
        return new CreateManufacturingOrderService(repositoryPort, billOfMaterialsRepositoryPort);
    }

    @Bean
    public MaterialStockRepositoryPort materialStockRepositoryPort(DataMaterialStockRepository stockRepository) {
        return new MaterialStockRepositoryAdapter(stockRepository);
    }

    @Bean
    public CheckProductAvailabilityPort checkProductAvailabilityPort(
            BillOfMaterialsRepositoryPort billOfMaterialsRepositoryPort,
            MaterialStockRepositoryPort materialStockRepositoryPort
    ) {
        return new CheckProductAvailabilityHandler(billOfMaterialsRepositoryPort, materialStockRepositoryPort);
    }

    @Bean
    public StartManufacturingOrderService startManufacturingOrderHandler(
            ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort,
            MaterialStockRepositoryPort materialStockRepositoryPort
    ) {
        return new StartManufacturingOrderService(manufacturingOrderRepositoryPort, materialStockRepositoryPort);
    }

    @Bean
    public FinishManufacturingOrderHandler finishManufacturingOrderHandler(
            ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort
    ) {
        return new FinishManufacturingOrderHandler(manufacturingOrderRepositoryPort);
    }

    @Bean
    public CancelManufacturingOrderService cancelManufacturingOrderHandler(
            ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort
    ) {
        return new CancelManufacturingOrderService(manufacturingOrderRepositoryPort);
    }

    @Bean
    public ManufacturingOrderFinishedEventPublisherPort manufacturingOrderFinishedEventPublisherPort(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        return new KafkaManufacturingOrderFinishedEventPublisher(kafkaTemplate, objectMapper);
    }

    @Bean
    public CheckManufacturingOrderStatusHandler checkManufacturingOrderStatusHandler(
            ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort,
            ManufacturingOrderFinishedEventPublisherPort eventPublisherPort
    ) {
        return new CheckManufacturingOrderStatusHandler(manufacturingOrderRepositoryPort, eventPublisherPort);
    }
}
