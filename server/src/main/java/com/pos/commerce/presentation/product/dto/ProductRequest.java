package com.pos.commerce.presentation.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.pos.commerce.domain.product.ProductStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String productCode;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private ProductStatus status;
}




