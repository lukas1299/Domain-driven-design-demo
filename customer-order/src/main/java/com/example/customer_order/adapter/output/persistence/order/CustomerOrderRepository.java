package com.example.customer_order.adapter.output.persistence.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrderEntity, UUID> {}