package com.example.manufacturing_order.application.product;

import com.example.common.api.product.ProductAvailabilityResponse;
import com.example.manufacturing_order.adapter.output.persistence.bom.BillOfMaterialsRepositoryPort;
import com.example.manufacturing_order.adapter.output.persistence.stock.MaterialStockRepositoryPort;
import com.example.manufacturing_order.domain.model.billOfMaterials.BillOfMaterials;
import com.example.manufacturing_order.domain.model.billOfMaterials.BomLine;
import com.example.manufacturing_order.domain.model.stock.MaterialStock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckProductAvailabilityHandlerTest {

    @Mock
    private BillOfMaterialsRepositoryPort billOfMaterialsRepositoryPort;

    @Mock
    private MaterialStockRepositoryPort materialStockRepositoryPort;

    @InjectMocks
    private CheckProductAvailabilityHandler handler;

    @Test
    void check_shouldReturnFalseWhenBomNotFound() {
        when(billOfMaterialsRepositoryPort.findByProductSku("UNKNOWN")).thenReturn(Optional.empty());

        ProductAvailabilityResponse response = handler.checkSubProductsAvailability("UNKNOWN");

        assertThat(response.available()).isFalse();
        assertThat(response.productSku()).isEqualTo("UNKNOWN");
    }

    @Test
    void check_shouldReturnTrueWhenStockIsSufficient() {
        BillOfMaterials bom = new BillOfMaterials("PRODUCT-1", List.of(new BomLine("SEMI-1", 2)));
        when(billOfMaterialsRepositoryPort.findByProductSku("PRODUCT-1")).thenReturn(Optional.of(bom));
        when(materialStockRepositoryPort.findByMaterialSkus(List.of("SEMI-1")))
                .thenReturn(List.of(MaterialStock.createNew("SEMI-1", 10)));

        ProductAvailabilityResponse response = handler.checkSubProductsAvailability("PRODUCT-1");

        assertThat(response.available()).isTrue();
    }

    @Test
    void check_shouldReturnFalseWhenStockIsInsufficient() {
        BillOfMaterials bom = new BillOfMaterials("PRODUCT-1", List.of(new BomLine("SEMI-1", 5)));
        when(billOfMaterialsRepositoryPort.findByProductSku("PRODUCT-1")).thenReturn(Optional.of(bom));
        when(materialStockRepositoryPort.findByMaterialSkus(List.of("SEMI-1")))
                .thenReturn(List.of(MaterialStock.createNew("SEMI-1", 2)));

        ProductAvailabilityResponse response = handler.checkSubProductsAvailability("PRODUCT-1");

        assertThat(response.available()).isFalse();
    }
}
