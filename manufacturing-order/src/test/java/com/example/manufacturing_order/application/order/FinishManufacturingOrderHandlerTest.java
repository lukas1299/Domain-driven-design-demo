package com.example.manufacturing_order.application.order;

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinishManufacturingOrderHandlerTest {

    @Mock
    private ManufacturingOrderRepositoryPort manufacturingOrderRepositoryPort;

    @InjectMocks
    private FinishManufacturingOrderHandler handler;

    @Test
    void handle_shouldFinishInProgressOrder() {
        ManufacturingOrder order = inProgressOrder();
        when(manufacturingOrderRepositoryPort.findFirstInProgress()).thenReturn(Optional.of(order));

        handler.handle();

        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.FINISHED);
        verify(manufacturingOrderRepositoryPort).save(order);
    }

    @Test
    void handle_shouldThrowWhenNoInProgressOrder() {
        when(manufacturingOrderRepositoryPort.findFirstInProgress()).thenReturn(Optional.empty());

        assertThatThrownBy(handler::handle)
                .isInstanceOf(IllegalArgumentException.class);

        verify(manufacturingOrderRepositoryPort, never()).save(any());
    }

    private ManufacturingOrder inProgressOrder() {
        ManufacturingOrder order = ManufacturingOrder.createNew(
                UUID.randomUUID().toString(),
                "PRODUCT-1",
                1,
                List.of(new MaterialRequirement("SEMI-1", 1))
        );
        order.startProduction();
        return order;
    }
}
