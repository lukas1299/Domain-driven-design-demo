package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.adapter.output.persistence.stock.MaterialStockRepositoryPort;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import com.example.manufacturing_order.domain.model.order.ManufacturingStatus;
import com.example.manufacturing_order.domain.model.order.MaterialRequirement;
import com.example.manufacturing_order.domain.model.stock.InsufficientMaterialStockException;
import com.example.manufacturing_order.domain.model.stock.MaterialStock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartManufacturingOrderHandlerTest {

    @Mock
    private ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;

    @Mock
    private MaterialStockRepositoryPort materialStockRepositoryPort;

    @InjectMocks
    private StartManufacturingOrderService service;

    @Test
    void handle_shouldStartProductionWhenStockIsSufficient() {
        ManufacturingOrder order = ManufacturingOrder.createNew(
                UUID.randomUUID().toString(),
                "PRODUCT-1",
                2,
                List.of(new MaterialRequirement("SEMI-1", 4))
        );
        MaterialStock stock = MaterialStock.createNew("SEMI-1", 10);

        when(manufacturingOrderRepositoryPort.findFirstPending()).thenReturn(Optional.of(order));
        when(materialStockRepositoryPort.findByMaterialSkus(List.of("SEMI-1"))).thenReturn(List.of(stock));

        service.handle();

        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.IN_PROGRESS);
        assertThat(stock.getQuantity()).isEqualTo(6);
        verify(materialStockRepositoryPort).save(stock);
        verify(manufacturingOrderRepositoryPort).save(order);
    }

    @Test
    void handle_shouldThrowWhenNoPendingOrder() {
        when(manufacturingOrderRepositoryPort.findFirstPending()).thenReturn(Optional.empty());

        assertThatThrownBy(service::handle)
                .isInstanceOf(IllegalArgumentException.class);

        verify(materialStockRepositoryPort, never()).save(any());
    }

    @Test
    void handle_shouldThrowWhenStockIsMissing() {
        ManufacturingOrder order = ManufacturingOrder.createNew(
                UUID.randomUUID().toString(),
                "PRODUCT-1",
                1,
                List.of(new MaterialRequirement("SEMI-1", 5))
        );
        when(manufacturingOrderRepositoryPort.findFirstPending()).thenReturn(Optional.of(order));
        when(materialStockRepositoryPort.findByMaterialSkus(List.of("SEMI-1"))).thenReturn(List.of());

        assertThatThrownBy(service::handle)
                .isInstanceOf(InsufficientMaterialStockException.class);

        verify(manufacturingOrderRepositoryPort, never()).save(any());
    }
}
