package com.example.manufacturing_order.adapter.output.persistence.bom;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(
        name = "product_bom_lines",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_sku", "semi_product_sku"})
)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductBomLineEntity {
    @Id
    private UUID id;
    @Column(name = "product_sku", nullable = false)
    private String productSku;
    @Column(name = "semi_product_sku", nullable = false)
    private String semiProductSku;
    @Column(name = "quantity_per_unit", nullable = false)
    private int quantityPerUnit;
}
