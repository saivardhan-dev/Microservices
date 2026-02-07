package com.microservices.orderservice;

import com.microservices.orderservice.config.InventoryClient;
import com.microservices.orderservice.exceptions.OrderNotFoundException;
import com.microservices.orderservice.exceptions.OutOfStockException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository repo, InventoryClient inventoryClient) {
        this.repo = repo;
        this.inventoryClient = inventoryClient;
    }

    public List<OrderModel> getAll() {
        return repo.findAll();
    }

    public OrderModel getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + id));
    }

    public OrderModel create(OrderModel order) {
        // Basic validation
        if (order.getQty() <= 0) {
            throw new IllegalArgumentException("qty must be > 0");
        }
        if (order.getProductName() == null || order.getProductName().isBlank()) {
            throw new IllegalArgumentException("productName is required");
        }

        // 1) Check stock
        boolean available = inventoryClient.checkStock(order.getProductName(), order.getQty());
        if (!available) {
            throw new OutOfStockException("Out of stock for product: " + order.getProductName());
        }

        // 2) Reserve stock
        inventoryClient.reserveStock(order.getProductName(), order.getQty());

        // 3) Save order
        order.setStatus("CREATED");
        return repo.save(order);
    }

    public boolean delete(String id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
