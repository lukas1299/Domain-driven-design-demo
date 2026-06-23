package com.example.manufacturing_order.adapter.output.persistence.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "material_stock")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaterialStockEntity {
    @Id
    @Column(name = "material_sku", nullable = false)
    private String materialSku;
    @Column(nullable = false)
    private int quantity;
}
