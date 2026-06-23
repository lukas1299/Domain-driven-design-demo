package com.example.manufacturing_order.domain.model.stock;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaterialStockTest {

    @Test
    void createNew_shouldInitializeStock() {
        MaterialStock stock = MaterialStock.createNew("MAT-1", 10);

        assertThat(stock.getMaterialSku()).isEqualTo("MAT-1");
        assertThat(stock.getQuantity()).isEqualTo(10);
    }

    @Test
    void hasEnough_shouldReturnTrueWhenSufficient() {
        MaterialStock stock = MaterialStock.createNew("MAT-1", 10);

        assertThat(stock.hasEnough(5)).isTrue();
        assertThat(stock.hasEnough(10)).isTrue();
    }

    @Test
    void hasEnough_shouldReturnFalseWhenInsufficient() {
        MaterialStock stock = MaterialStock.createNew("MAT-1", 5);

        assertThat(stock.hasEnough(6)).isFalse();
        assertThat(stock.hasEnough(0)).isFalse();
    }

    @Test
    void consume_shouldReduceQuantity() {
        MaterialStock stock = MaterialStock.createNew("MAT-1", 10);

        stock.consume(4);

        assertThat(stock.getQuantity()).isEqualTo(6);
    }

    @Test
    void consume_shouldThrowWhenInsufficientStock() {
        MaterialStock stock = MaterialStock.createNew("MAT-1", 3);

        assertThatThrownBy(() -> stock.consume(5))
                .isInstanceOf(InsufficientMaterialStockException.class);
    }
}
