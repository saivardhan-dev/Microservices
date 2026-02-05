package com.microservices.inventory;

import com.microservices.inventory.exceptions.InsufficientStockException;
import com.microservices.inventory.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public List<InventoryModel> getAll() {
        return repo.findAll();
    }

    public InventoryModel getById(String productId) {
        return repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }

    public InventoryModel create(InventoryModel product) {
        return repo.save(product);
    }

    public InventoryModel updateQuantity(String productId, int newQuantity) {
        InventoryModel existing = getById(productId);
        existing.setAvailableQuantity(newQuantity);
        return repo.save(existing);
    }

    public boolean delete(String productId) {
        if (!repo.existsById(productId)) return false;
        repo.deleteById(productId);
        return true;
    }

    // --- Inventory specific endpoints (for Order Service usage) ---

    public boolean isInStock(String productName, int requiredQty) {
        InventoryModel item = repo.findByProductName(productName)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
        return item.getAvailableQuantity() >= requiredQty;
    }

    public InventoryModel reserveStock(String productName, int qty) {

        InventoryModel product = repo.findByProductName(productName)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found: " + productName)
                );

        if (qty <= 0) {
            throw new IllegalArgumentException("qty must be greater than 0");
        }

        if (product.getAvailableQuantity() < qty) {
            throw new InsufficientStockException(
                    "Not enough stock for " + productName +
                            ". Available=" + product.getAvailableQuantity() +
                            ", Requested=" + qty
            );
        }

        product.setAvailableQuantity(product.getAvailableQuantity() - qty);
        return repo.save(product);
    }
}