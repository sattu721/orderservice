package com.ecommerce.orderservice.service;

import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.dto.ProductDto;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderService {

	private final OrderRepository repository;
	private final ProductClientService productClientService;

	public OrderService(OrderRepository repository, ProductClientService productClientService) {
		this.repository = repository;
		this.productClientService = productClientService;
	}

	@CircuitBreaker(name = "productService", fallbackMethod = "fallback")
	public OrderEntity createOrder(OrderEntity order) {
		ProductDto product = productClientService.getProduct(order.getProductId());
		return repository.save(order);
	}

	public OrderEntity fallback(OrderEntity order, Exception ex) {
		System.out.println("Fallback triggered: Product service unavailable");
		return null;
	}

}
