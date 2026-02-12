package com.ecommerce.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.common.exception.OutOfStockException;
import com.ecommerce.common.exception.PaymentFailedException;
import com.ecommerce.orderservice.cutomexception.OrderException;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.feignclient.InventoryClient;
import com.ecommerce.orderservice.feignclient.PaymentClient;
import com.ecommerce.orderservice.records.PaymentRequest;
import com.ecommerce.orderservice.records.PaymentResponse;
import com.ecommerce.orderservice.records.ProductDto;
import com.ecommerce.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository repository;
	private final ProductClientService productClientService;
	private final InventoryClient inventoryClient;
	private final PaymentClient paymentClient;

	@Transactional
	public OrderEntity createOrder(OrderEntity order) {
		// Save order with PAYMENT_PENDING status
		order.setStatus(OrderStatus.PENDING.name());
		order = repository.save(order);
		if (order != null) {
			ProductDto product = productClientService.getProduct(order.getProductId());
			boolean isReserved = inventoryClient.reserveInventory(order.getProductId(), order.getQuantity());
			if (!isReserved) {
				throw new OutOfStockException(product.name());
			}
			order.setStatus(OrderStatus.INVENTORY_RESERVED.name());
			order = repository.save(order);

			PaymentRequest paymentRequest = new PaymentRequest(order.getId(), product.price(), "CARD");

			PaymentResponse paymentResponse = paymentClient.makePayment(paymentRequest);
			if (paymentResponse == null || !"SUCCESS".equalsIgnoreCase(paymentResponse.status())) {
				inventoryClient.reStoreStock(order.getProductId(), order.getQuantity());
				order.setStatus(OrderStatus.FAILED.name());
				repository.save(order);
				throw new PaymentFailedException();
			}
			order.setStatus(OrderStatus.CREATED.name());
			return repository.save(order);
		} else {
			throw new OrderException("ORDER_FAILED", "Order creation failed");
		}
	}
}