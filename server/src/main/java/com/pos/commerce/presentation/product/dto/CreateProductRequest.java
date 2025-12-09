package com.pos.commerce.presentation.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile; // ❗추가

import java.math.BigDecimal;
import com.pos.commerce.domain.product.ProductStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    /* 상품 코드 */
    private String code;            
    /* 상품 이름 */
    private String name;
    /* 상품 설명 */
    private String description;

    /* 상품 가격 */
    private BigDecimal price;
    /* 상품 재고 */
    private Integer stock;         
    /* 상품 상태 */
    private ProductStatus status;

    /* 상품 이미지 */
    private MultipartFile image;   

}