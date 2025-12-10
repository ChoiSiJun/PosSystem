package com.pos.commerce.application.product.command;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.pos.commerce.domain.product.ProductStatus;

public record UpdateProductCommand(
        Long productId,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        MultipartFile image,
        String imageUrl,
        ProductStatus status
) {
}





