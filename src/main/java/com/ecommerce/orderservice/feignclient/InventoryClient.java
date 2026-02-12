package com.ecommerce.orderservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/inventory/reserve/{productId}/{qty}")
    boolean reserveInventory(@PathVariable Long productId,
                       @PathVariable int qty);

    @PostMapping("/inventory/restore/{productId}/{qty}")
    void reStoreStock(@PathVariable Long productId,
                     @PathVariable int qty);
}

