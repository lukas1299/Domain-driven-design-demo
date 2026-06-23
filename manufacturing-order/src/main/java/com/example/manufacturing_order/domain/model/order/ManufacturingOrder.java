package com.example.manufacturing_order.domain.model.order;

import com.example.common.validator.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
public class ManufacturingOrder {
    private final ManufacturingOrderId id;
    private final String sourceOrderId;
    private final String productSku;
    private final int quantity;
    private final List<MaterialRequirement> materialRequirements;
    private ManufacturingStatus status;

    public static ManufacturingOrder createNew(
            String sourceOrderId,
            String productSku,
            int quantity,
            List<MaterialRequirement> materialRequirements
    ) {

        ValidateNotNullSourceOrderIdRule validateNotNullSourceOrderIdRule = new ValidateNotNullSourceOrderIdRule(sourceOrderId);
        ValidateSubProductsRule validateSubProductsRule = new ValidateSubProductsRule(productSku);
        ValidateQuantityRule validateQuantityRule = new ValidateQuantityRule(quantity);
        ValidateRequirementsRule validateRequirementsRule = new ValidateRequirementsRule(materialRequirements);

        ManufacturingOrderValidator validator = new ManufacturingOrderValidator(
                List.of(validateNotNullSourceOrderIdRule,
                        validateSubProductsRule,
                        validateQuantityRule,
                        validateRequirementsRule));
        validator.verify();

        return new ManufacturingOrder(
                ManufacturingOrderId.generate(),
                sourceOrderId,
                productSku,
                quantity,
                materialRequirements,
                ManufacturingStatus.PENDING
        );
    }

    public static ManufacturingOrder reconstitute(
            UUID id,
            String sourceOrderId,
            String productSku,
            int quantity,
            List<MaterialRequirement> materialRequirements,
            ManufacturingStatus status
    ) {
        return new ManufacturingOrder(
                new ManufacturingOrderId(id),
                sourceOrderId,
                productSku,
                quantity,
                materialRequirements,
                status
        );
    }

    public void startProduction() {
        if (this.status != ManufacturingStatus.PENDING) {
            throw new IllegalStateException("Można uruchomić produkcję tylko dla zleceń oczekujących.");
        }
        this.status = ManufacturingStatus.IN_PROGRESS;
    }

    public void finishProduction() {
        if (this.status != ManufacturingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Nie można zakończyć produkcji, która nie wystartowała.");
        }
        this.status = ManufacturingStatus.FINISHED;
    }

    public void cancelProduction() {
        if (this.status != ManufacturingStatus.PENDING) {
            throw new IllegalStateException("Nie można anulować produkcji o statusie: " + this.status);
        }
        this.status = ManufacturingStatus.CANCELED;
    }

    public void markAsNotified() {
        if (this.status != ManufacturingStatus.FINISHED) {
            throw new IllegalStateException("Można oznaczyć jako powiadomione tylko zlecenia zakończone.");
        }
        this.status = ManufacturingStatus.NOTIFIED;
    }

    public ManufacturingOrderId getId() {
        return id;
    }

    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public String getProductSku() {
        return productSku;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<MaterialRequirement> getMaterialRequirements() {
        return materialRequirements;
    }

    public ManufacturingStatus getStatus() {
        return status;
    }
}
