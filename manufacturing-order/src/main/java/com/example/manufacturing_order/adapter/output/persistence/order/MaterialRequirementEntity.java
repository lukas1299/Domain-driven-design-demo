package com.example.manufacturing_order.adapter.output.persistence.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "manufacturing_order_material_requirements")
@NoArgsConstructor
@Getter
@Setter
public class MaterialRequirementEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturing_order_id", nullable = false)
    private ManufacturingOrderEntity manufacturingOrder;
    @Column(name = "semi_product_sku", nullable = false)
    private String semiProductSku;
    @Column(name = "required_quantity", nullable = false)
    private int requiredQuantity;
}
