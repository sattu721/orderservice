package com.ecommerce.orderservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.orderservice.records.PaymentRequest;
import com.ecommerce.orderservice.records.PaymentResponse;

@FeignClient(name = "payment-service",url="http://localhost:8085")
public interface PaymentClient {

    @PostMapping("/payments")
    PaymentResponse makePayment(PaymentRequest request);
}