package com.example.customer_order.adapter.output.manufacturing;

import com.example.common.api.product.ProductAvailabilityResponse;
import com.example.customer_order.domain.model.order.exception.ManufacturingServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class HttpProductAvailabilityAdapter implements ProductAvailabilityPort {

    private final RestClient restClient;

    public HttpProductAvailabilityAdapter(RestClient manufacturingRestClient) {
        this.restClient = manufacturingRestClient;
    }

    @Override
    public boolean isProductAvailable(String productSku) {
        try {
            ProductAvailabilityResponse response = restClient.get()
                    .uri("/v1/products/{sku}/availability", productSku)
                    .retrieve()
                    .body(ProductAvailabilityResponse.class);

            return response != null && response.available();
        } catch (RestClientException exception) {
            throw new ManufacturingServiceUnavailableException(exception);
        }
    }
}
