package com.ecommerce.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.orderservice.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
