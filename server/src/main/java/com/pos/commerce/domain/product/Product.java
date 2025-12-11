package com.pos.commerce.domain.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 매장 코드 */
    @Column(nullable = false)
    private String shopCode;

    /* 상품 코드 */
    @Column(nullable = false, unique = true)
    private String code;

    /* 상품 이름 */
    @Column(nullable = false)
    private String name;

    /* 설명 */
    @Column(length = 1000)
    private String description;

    /* 가격 */
    @Column(nullable = false)
    private BigDecimal price;

    /* 재고 수량 */
    @Column(nullable = false)
    private Integer stock;

    /* 상품 이미지 */
    @Column(nullable = true)
    private String image_url;

    /* 상품 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateStock(Integer stock) {
        this.stock = stock;
    }

    public void decreaseStock(Integer stock) {
        if (this.stock < stock) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stock -= stock;
    }

    public void increaseStock(Integer stock) {
        this.stock += stock;
    }

    public void activate() {
        this.status = ProductStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }
}

