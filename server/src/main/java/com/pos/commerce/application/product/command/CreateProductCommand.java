package com.pos.commerce.application.product.command;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.pos.commerce.domain.product.ProductStatus;

public record CreateProductCommand(
       String code,
       String name,
       String description,
       BigDecimal price,
       Integer stock,
       ProductStatus status,
       MultipartFile image
) {
}





