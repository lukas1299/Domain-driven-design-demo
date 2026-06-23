package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.messaging.order.ManufacturingOrderFinishedEventPublisherPort;
import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import com.example.manufacturing_order.domain.model.order.ManufacturingStatus;
import com.example.manufacturing_order.domain.model.order.MaterialRequirement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckManufacturingOrderStatusHandlerTest {

    @Mock
    private ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;

    @Mock
    private ManufacturingOrderFinishedEventPublisherPort eventPublisherPort;

    @InjectMocks
    private CheckManufacturingOrderStatusHandler handler;

    @Test
    void handle_shouldPublishEventAndMarkAsNotified() {
        ManufacturingOrder order = finishedOrder();
        when(manufacturingOrderRepositoryPort.findFirstFinished()).thenReturn(Optional.of(order));

        handler.handle();

        verify(eventPublisherPort).publish(order);
        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.NOTIFIED);
        verify(manufacturingOrderRepositoryPort).save(order);
    }

    @Test
    void handle_shouldDoNothingWhenNoFinishedOrder() {
        when(manufacturingOrderRepositoryPort.findFirstFinished()).thenReturn(Optional.empty());

        handler.handle();

        verify(eventPublisherPort, never()).publish(any());
        verify(manufacturingOrderRepositoryPort, never()).save(any());
    }

    private ManufacturingOrder finishedOrder() {
        ManufacturingOrder order = ManufacturingOrder.createNew(
                UUID.randomUUID().toString(),
                "PRODUCT-1",
                1,
                List.of(new MaterialRequirement("SEMI-1", 1))
        );
        order.startProduction();
        order.finishProduction();
        return order;
    }
}
