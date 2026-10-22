package com.example.app;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-lines")
public class OrderLineController {

    private final OrderLineService service;

    public OrderLineController(OrderLineService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<OrderLine> findAll() {
        return service.findAll();
    }

    @GetMapping("/{orderId}/{lineId}")
    public ResponseEntity<OrderLine> find(@PathVariable String orderId, @PathVariable String lineId) {
        return service.find(orderId, lineId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderLine> create(@RequestBody OrderLine line) {
        OrderLine saved = service.upsert(line);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{orderId}/{lineId}")
    public ResponseEntity<OrderLine> update(
            @PathVariable String orderId,
            @PathVariable String lineId,
            @RequestBody OrderLine line) {
        OrderLine saved = service.upsert(
                new OrderLine(orderId, lineId, line.productId(), line.quantity(), line.price()));
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{orderId}/{lineId}")
    public ResponseEntity<Void> delete(@PathVariable String orderId, @PathVariable String lineId) {
        return service.delete(orderId, lineId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidInput(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
