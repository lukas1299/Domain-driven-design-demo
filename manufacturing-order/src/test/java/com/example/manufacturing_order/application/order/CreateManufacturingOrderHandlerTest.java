package com.example.manufacturing_order.application.order;

import com.example.manufacturing_order.adapter.output.persistence.bom.BillOfMaterialsRepositoryPort;
import com.example.manufacturing_order.adapter.output.persistence.order.ManufacturingOrderRepositoryPort;
import com.example.manufacturing_order.domain.model.billOfMaterials.BillOfMaterials;
import com.example.manufacturing_order.domain.model.billOfMaterials.BillOfMaterialsNotFoundException;
import com.example.manufacturing_order.domain.model.billOfMaterials.BomLine;
import com.example.manufacturing_order.domain.model.order.ManufacturingOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateManufacturingOrderHandlerTest {

    @Mock
    private ManufacturingOrderRepositoryPort repositoryPort;

    @Mock
    private BillOfMaterialsRepositoryPort billOfMaterialsRepositoryPort;

    @InjectMocks
    private CreateManufacturingOrderService service;

    @Test
    void handle_shouldCreateManufacturingOrder() {
        CreateManufacturingOrderCommand command =
                new CreateManufacturingOrderCommand("order-1", "PRODUCT-1", 2);
        BillOfMaterials bom = new BillOfMaterials("PRODUCT-1", List.of(new BomLine("SEMI-1", 3)));

        when(repositoryPort.findBySourceOrderId("order-1")).thenReturn(Optional.empty());
        when(billOfMaterialsRepositoryPort.findByProductSku("PRODUCT-1")).thenReturn(Optional.of(bom));

        service.handle(command);

        ArgumentCaptor<ManufacturingOrder> captor = ArgumentCaptor.forClass(ManufacturingOrder.class);
        verify(repositoryPort).save(captor.capture());
        assertThat(captor.getValue().getSourceOrderId()).isEqualTo("order-1");
        assertThat(captor.getValue().getProductSku()).isEqualTo("PRODUCT-1");
        assertThat(captor.getValue().getQuantity()).isEqualTo(2);
    }

    @Test
    void handle_shouldThrowWhenBomNotFound() {
        when(repositoryPort.findBySourceOrderId("order-1")).thenReturn(Optional.empty());
        when(billOfMaterialsRepositoryPort.findByProductSku("PRODUCT-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.handle(new CreateManufacturingOrderCommand("order-1", "PRODUCT-1", 2)))
                .isInstanceOf(BillOfMaterialsNotFoundException.class);

        verify(repositoryPort, never()).save(any());
    }
}
