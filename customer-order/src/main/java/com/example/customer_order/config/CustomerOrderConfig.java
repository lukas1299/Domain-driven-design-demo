package com.example.customer_order.config;

import com.example.customer_order.adapter.input.rest.order.CreateCustomerOrderController;
import com.example.customer_order.adapter.output.manufacturing.ProductAvailabilityPort;
import com.example.customer_order.adapter.output.outbox.CustomerOrderEventPublisher;
import com.example.customer_order.adapter.output.persistence.cancel.CancelCustomerOrderPort;
import com.example.customer_order.adapter.output.persistence.order.CustomerOrderRepositoryPort;
import com.example.customer_order.adapter.output.persistence.order.DeleteCustomerOrderRepositoryPort;
import com.example.customer_order.adapter.output.persistence.order.UpdateCustomerOrderRepositoryPort;
import com.example.customer_order.adapter.output.persistence.payment.PayCustomerOrderRepositoryPort;
import com.example.customer_order.application.cancel.CancelOrderService;
import com.example.customer_order.application.order.CreateCustomerOrderService;
import com.example.customer_order.application.order.DeleteCustomerOrderHandler;
import com.example.customer_order.application.order.UpdateCustomerOrderHandler;
import com.example.customer_order.application.payment.PayCustomerOrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CustomerOrderConfig {

    @Bean
    public RestClient manufacturingRestClient(@Value("${manufacturing.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public CreateCustomerOrderService createCustomerOrderHandler(
            CustomerOrderRepositoryPort repositoryPort,
            ProductAvailabilityPort productAvailabilityPort) {
        return new CreateCustomerOrderService(repositoryPort, productAvailabilityPort);
    }

    @Bean
    public UpdateCustomerOrderHandler updateCustomerOrderHandler(
            UpdateCustomerOrderRepositoryPort repositoryPort,
            ProductAvailabilityPort productAvailabilityPort) {
        return new UpdateCustomerOrderHandler(repositoryPort, productAvailabilityPort);
    }

    @Bean
    public DeleteCustomerOrderHandler deleteCustomerOrderHandler(
            DeleteCustomerOrderRepositoryPort repositoryPort) {
        return new DeleteCustomerOrderHandler(repositoryPort);
    }

    @Bean
    public PayCustomerOrderService createPayCustomerOrderService(
            PayCustomerOrderRepositoryPort repositoryPort, CustomerOrderEventPublisher publisher) {
        return new PayCustomerOrderService(repositoryPort, publisher);
    }

    @Bean
    public CancelOrderService createCancelOrderService(
            CancelCustomerOrderPort cancelCustomerOrderPort) {
        return new CancelOrderService(cancelCustomerOrderPort);
    }

    @Bean
    public CreateCustomerOrderController createCustomerOrderController(CreateCustomerOrderService service) {
        return new CreateCustomerOrderController(service);
    }
}