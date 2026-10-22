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
        lines.put(key(line.orderId(), line.lineId()), line);
        return line;
    }

    public boolean delete(String orderId, String lineId) {
        return lines.remove(key(orderId, lineId)) != null;
    }

    private String key(String orderId, String lineId) {
        return orderId + "\u0000" + lineId;
    }
}
