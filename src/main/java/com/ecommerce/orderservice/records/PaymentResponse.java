package com.ecommerce.orderservice.records;

public record PaymentResponse(Long id, Long orderId, String status, String transactionId) {	
   
}