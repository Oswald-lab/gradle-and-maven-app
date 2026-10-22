package com.example.app;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class OrderLineService {

    private final ConcurrentMap<String, OrderLine> lines = new ConcurrentHashMap<>();

    public Collection<OrderLine> findAll() {
        return lines.values();
    }

    public Optional<OrderLine> find(String orderId, String lineId) {
        return Optional.ofNullable(lines.get(key(orderId, lineId)));
    }

    public OrderLine upsert(OrderLine line) {
        validate(line);
        lines.put(key(line.orderId(), line.lineId()), line);
        return line;
    }

    private void validate(OrderLine line) {
        if (line == null) {
            throw new IllegalArgumentException("Order line is required");
        }
        if (isBlank(line.orderId())) {
            throw new IllegalArgumentException("orderId is required");
        }
        if (isBlank(line.lineId())) {
            throw new IllegalArgumentException("lineId is required");
        }
        if (isBlank(line.productId())) {
            throw new IllegalArgumentException("productId is required");
        }
        if (line.quantity() <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
        if (line.price() == null) {
            throw new IllegalArgumentException("price is required");
        }
        if (line.price().signum() < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public boolean delete(String orderId, String lineId) {
        return lines.remove(key(orderId, lineId)) != null;
    }

    private String key(String orderId, String lineId) {
        return orderId + "\u0000" + lineId;
    }
}
