package com.example.manufacturing_order.domain.model.billOfMaterials;

import com.example.manufacturing_order.domain.model.order.MaterialRequirement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BillOfMaterialsTest {

    @Test
    void calculateRequirements_shouldMultiplyByOrderQuantity() {
        BillOfMaterials bom = new BillOfMaterials(
                "PRODUCT-1",
                List.of(
                        new BomLine("SEMI-1", 2),
                        new BomLine("SEMI-2", 3)
                )
        );

        List<MaterialRequirement> requirements = bom.calculateRequirements(4);

        assertThat(requirements).containsExactly(
                new MaterialRequirement("SEMI-1", 8),
                new MaterialRequirement("SEMI-2", 12)
        );
    }

    @Test
    void calculateRequirements_shouldRejectNonPositiveQuantity() {
        BillOfMaterials bom = new BillOfMaterials("PRODUCT-1", List.of(new BomLine("SEMI-1", 1)));

        assertThatThrownBy(() -> bom.calculateRequirements(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_shouldValidateInput() {
        assertThatThrownBy(() -> new BillOfMaterials(" ", List.of(new BomLine("SEMI-1", 1))))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new BillOfMaterials("PRODUCT-1", List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
