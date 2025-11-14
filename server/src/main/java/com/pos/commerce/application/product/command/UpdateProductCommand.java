package com.pos.commerce.application.product.command;

import java.math.BigDecimal;

import com.pos.commerce.domain.product.ProductStatus;

public record UpdateProductCommand(
        Long productId,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        ProductStatus status
) {
}





