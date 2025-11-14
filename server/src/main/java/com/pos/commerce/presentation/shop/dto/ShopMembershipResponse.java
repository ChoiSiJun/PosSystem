package com.pos.commerce.presentation.shop.dto;

import java.time.LocalDateTime;

import com.pos.commerce.domain.shop.ShopMemberRole;
import com.pos.commerce.domain.shop.ShopMembership;
import com.pos.commerce.domain.shop.ShopMembershipStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopMembershipResponse {
    private Long id;
    private Long userId;
    private ShopMemberRole role;
    private ShopMembershipStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime decidedAt;

    public static ShopMembershipResponse from(ShopMembership membership) {
        return ShopMembershipResponse.builder()
                .id(membership.getId())
                .userId(membership.getUser().getId())
                .role(membership.getRole())
                .status(membership.getStatus())
                .createdAt(membership.getCreatedAt())
                .decidedAt(membership.getDecidedAt())
                .build();
    }
}





