package com.pos.commerce.domain.shop;

import java.time.LocalDateTime;

import com.pos.commerce.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_memberships")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShopMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShopMembershipStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime decidedAt;

    public void assignShop(Shop shop) {
        this.shop = shop;
    }

    public void approve() {
        this.status = ShopMembershipStatus.APPROVED;
        this.decidedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = ShopMembershipStatus.REJECTED;
        this.decidedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}





