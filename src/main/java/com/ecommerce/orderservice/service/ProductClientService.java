package com.ecommerce.orderservice.service;

import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.cutomexception.OrderException;
import com.ecommerce.orderservice.feignclient.ProductClient;
import com.ecommerce.orderservice.records.ProductDto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ProductClientService {

	private final ProductClient productClient;

	public ProductClientService(ProductClient productClient) {
		this.productClient = productClient;
	}

	@Retry(name = "productServiceRetry")
	@CircuitBreaker(name = "productService", fallbackMethod = "fallback")
	public ProductDto getProduct(Long productId) {
		System.out.println("Feign call attempt");
		return productClient.getProduct(productId);
	}

	public ProductDto fallback(Long productId, Exception t) {
		System.out.println("Fallback triggered: Product service unavailable");
		throw new OrderException("SERVICE_UNAVAILABLE",
				"product service is currently unavailable. Please try again later.");
	}

}