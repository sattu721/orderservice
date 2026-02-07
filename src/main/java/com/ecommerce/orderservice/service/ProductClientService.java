package com.ecommerce.orderservice.service;

import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.dto.ProductDto;
import com.ecommerce.orderservice.feignclient.ProductClient;

import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ProductClientService {

    private final ProductClient productClient;

    public ProductClientService(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Retry(name = "productServiceRetry")
    public ProductDto getProduct(Long productId) {
        System.out.println("Feign call attempt");
        return productClient.getProduct(productId);
    }
}
