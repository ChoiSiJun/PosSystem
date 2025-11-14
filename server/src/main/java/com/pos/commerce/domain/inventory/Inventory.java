package com.pos.commerce.domain.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (reservedQuantity == null) {
            reservedQuantity = 0;
        }
        calculateAvailableQuantity();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateAvailableQuantity();
    }

    private void calculateAvailableQuantity() {
        this.availableQuantity = this.quantity - this.reservedQuantity;
    }

    public void increaseQuantity(Integer amount) {
        this.quantity += amount;
        calculateAvailableQuantity();
    }

    public void decreaseQuantity(Integer amount) {
        if (this.quantity < amount) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.quantity -= amount;
        calculateAvailableQuantity();
    }

    public void reserveQuantity(Integer amount) {
        if (this.availableQuantity < amount) {
            throw new IllegalStateException("예약 가능한 재고가 부족합니다.");
        }
        this.reservedQuantity += amount;
        calculateAvailableQuantity();
    }

    public void releaseReservation(Integer amount) {
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("예약된 재고가 부족합니다.");
        }
        this.reservedQuantity -= amount;
        calculateAvailableQuantity();
    }

    public void confirmReservation(Integer amount) {
        releaseReservation(amount);
        decreaseQuantity(amount);
    }
}

