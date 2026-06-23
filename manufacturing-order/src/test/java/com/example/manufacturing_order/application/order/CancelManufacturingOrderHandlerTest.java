package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import com.example.manufacturing_order.domain.model.order.ManufacturingStatus;
import com.example.manufacturing_order.domain.model.order.MaterialRequirement;
import com.example.manufacturing_order.domain.model.order.exception.ManufacturingOrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class CancelManufacturingOrderHandlerTest {

    @Mock
    private ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;

    @InjectMocks
    private CancelManufacturingOrderService service;

    @Test
    void handle_shouldCancelPendingOrder() {
        UUID sourceOrderId = UUID.randomUUID();
        ManufacturingOrder order = ManufacturingOrder.createNew(
                sourceOrderId.toString(),
                "PRODUCT-1",
                1,
                List.of(new MaterialRequirement("SEMI-1", 1))
        );
        when(manufacturingOrderRepositoryPort.findBySourceOrderId(sourceOrderId.toString()))
                .thenReturn(Optional.of(order));

        service.handle(sourceOrderId);

        ArgumentCaptor<ManufacturingOrder> captor = ArgumentCaptor.forClass(ManufacturingOrder.class);
        verify(manufacturingOrderRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(ManufacturingStatus.CANCELED);
    }

    @Test
    void handle_shouldThrowWhenOrderNotFound() {
        UUID sourceOrderId = UUID.randomUUID();
        when(manufacturingOrderRepositoryPort.findBySourceOrderId(sourceOrderId.toString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(sourceOrderId))
                .isInstanceOf(ManufacturingOrderNotFoundException.class);

        verify(manufacturingOrderRepositoryPort, never()).save(any());
    }
}
