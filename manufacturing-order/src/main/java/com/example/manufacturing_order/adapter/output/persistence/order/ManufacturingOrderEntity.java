package com.example.manufacturing_order.adapter.output.persistence.order;

import com.example.manufacturing_order.domain.model.order.ManufacturingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "manufacturing_orders")
@NoArgsConstructor
@Getter
@Setter
public class ManufacturingOrderEntity {
    @Id
    private UUID id;
    @Column(name = "source_order_id", nullable = false, unique = true)
    private String sourceOrderId;
    @Column(name = "product_sku", nullable = false)
    private String productSku;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private ManufacturingStatus status;
    @OneToMany(mappedBy = "manufacturingOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialRequirementEntity> materialRequirements = new ArrayList<>();
}
