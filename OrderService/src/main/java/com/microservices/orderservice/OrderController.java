package com.microservices.orderservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderModel> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<OrderModel> create(@RequestBody OrderModel order) {
        OrderModel saved = service.create(order);
        return ResponseEntity.created(URI.create("/orders/" + saved.getId()))
                .body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        if (!deleted) {
            return ResponseEntity.status(404).body(Map.of("message", "Order not found: " + id));
        }
        return ResponseEntity.ok(Map.of("message", "Order deleted successfully", "id", id));
    }
}
