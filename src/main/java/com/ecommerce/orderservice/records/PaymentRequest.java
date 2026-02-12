package com.ecommerce.orderservice.records;

public record PaymentRequest(Long orderId, Double amount, String paymentMode) {

}
