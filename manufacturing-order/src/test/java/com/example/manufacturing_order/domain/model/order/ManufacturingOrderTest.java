package com.example.manufacturing_order.domain.model.order;

import com.example.common.validator.RuleValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ManufacturingOrderTest {

    private static final List<MaterialRequirement> REQUIREMENTS =
            List.of(new MaterialRequirement("SEMI-1", 2));

    @Test
    void createNew_shouldCreatePendingOrder() {
        ManufacturingOrder order = ManufacturingOrder.createNew(
                UUID.randomUUID().toString(), "PRODUCT-1", 3, REQUIREMENTS);

        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.PENDING);
        assertThat(order.getProductSku()).isEqualTo("PRODUCT-1");
        assertThat(order.getQuantity()).isEqualTo(3);
        assertThat(order.getMaterialRequirements()).isEqualTo(REQUIREMENTS);
    }

    @Test
    void createNew_shouldValidateInput() {
        assertThatThrownBy(() -> ManufacturingOrder.createNew(null, "PRODUCT-1", 1, REQUIREMENTS))
                .isInstanceOf(RuleValidationException.class);
        assertThatThrownBy(() -> ManufacturingOrder.createNew("order-1", "  ", 1, REQUIREMENTS))
                .isInstanceOf(RuleValidationException.class);
        assertThatThrownBy(() -> ManufacturingOrder.createNew("order-1", "PRODUCT-1", 0, REQUIREMENTS))
                .isInstanceOf(RuleValidationException.class);
        assertThatThrownBy(() -> ManufacturingOrder.createNew("order-1", "PRODUCT-1", 1, List.of()))
                .isInstanceOf(RuleValidationException.class);
    }

    @Test
    void startProduction_shouldTransitionFromPendingToInProgress() {
        ManufacturingOrder order = pendingOrder();

        order.startProduction();

        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.IN_PROGRESS);
    }

    @Test
    void startProduction_shouldRejectWhenNotPending() {
        ManufacturingOrder order = pendingOrder();
        order.startProduction();

        assertThatThrownBy(order::startProduction)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void finishProduction_shouldTransitionFromInProgressToFinished() {
        ManufacturingOrder order = inProgressOrder();

        order.finishProduction();

        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.FINISHED);
    }

    @Test
    void finishProduction_shouldRejectWhenNotInProgress() {
        ManufacturingOrder order = pendingOrder();

        assertThatThrownBy(order::finishProduction)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void cancelProduction_shouldTransitionFromPendingToCanceled() {
        ManufacturingOrder order = pendingOrder();

        order.cancelProduction();

        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.CANCELED);
    }

    @Test
    void cancelProduction_shouldRejectWhenNotPending() {
        ManufacturingOrder order = inProgressOrder();

        assertThatThrownBy(order::cancelProduction)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void markAsNotified_shouldTransitionFromFinishedToNotified() {
        ManufacturingOrder order = finishedOrder();

        order.markAsNotified();

        assertThat(order.getStatus()).isEqualTo(ManufacturingStatus.NOTIFIED);
    }

    @Test
    void markAsNotified_shouldRejectWhenNotFinished() {
        ManufacturingOrder order = pendingOrder();

        assertThatThrownBy(order::markAsNotified)
                .isInstanceOf(IllegalStateException.class);
    }

    private ManufacturingOrder pendingOrder() {
        return ManufacturingOrder.createNew(
                UUID.randomUUID().toString(), "PRODUCT-1", 2, REQUIREMENTS);
    }

    private ManufacturingOrder inProgressOrder() {
        ManufacturingOrder order = pendingOrder();
        order.startProduction();
        return order;
    }

    private ManufacturingOrder finishedOrder() {
        ManufacturingOrder order = inProgressOrder();
        order.finishProduction();
        return order;
    }
}
