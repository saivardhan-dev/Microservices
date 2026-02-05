package com.microservices.inventory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // ✅ GET /inventory -> list all products
    @GetMapping
    public List<InventoryModel> getAll() {
        return service.getAll();
    }

    // ✅ GET /inventory/{id} -> get product by id
    @GetMapping("/{id}")
    public ResponseEntity<InventoryModel> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ POST /inventory -> create product
    @PostMapping
    public ResponseEntity<InventoryModel> create(@RequestBody InventoryModel product) {
        InventoryModel saved = service.create(product);
        return ResponseEntity.created(URI.create("/inventory/" + saved.getProductId()))
                .body(saved);
    }

    // ✅ PUT /inventory/{id}/quantity?value=10 -> update quantity
    @PutMapping("/{id}/quantity")
    public ResponseEntity<InventoryModel> updateQuantity(@PathVariable String id,
                                                         @RequestParam int value) {
        return ResponseEntity.ok(service.updateQuantity(id, value));
    }

    // ✅ DELETE /inventory/{id} -> delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        if (!deleted) {
            return ResponseEntity.status(404).body(Map.of("message", "Product not found: " + id));
        }
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully", "id", id));
    }

    // ---------------------------
    // Inventory APIs for Orders
    // ---------------------------

    // ✅ GET /inventory/check?productName=Phone&qty=2
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkStock(@RequestParam String productName,
                                                          @RequestParam int qty) {
        boolean available = service.isInStock(productName, qty);
        return ResponseEntity.ok(Map.of(
                "productName", productName,
                "requestedQty", qty,
                "available", available
        ));
    }

    // ✅ POST /inventory/reserve?productName=Phone&qty=2
    @PostMapping("/reserve")
    public ResponseEntity<Map<String, Object>> reserveStock(@RequestParam String productName,
                                                            @RequestParam int qty) {
        InventoryModel updated = service.reserveStock(productName, qty);
        return ResponseEntity.ok(Map.of(
                "message", "Stock reserved",
                "productName", updated.getProductName(),
                "remainingQty", updated.getAvailableQuantity()
        ));
    }
}
