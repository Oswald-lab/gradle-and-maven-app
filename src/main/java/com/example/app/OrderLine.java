package com.example.app;

import java.math.BigDecimal;

public record OrderLine(
        String orderId,
        String lineId,
        String productId,
        int quantity,
        BigDecimal price
) {
}
