package com.pos.commerce.presentation.shop.dto;

import com.pos.commerce.domain.shop.Shop;
import com.pos.commerce.domain.shop.ShopStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private ShopStatus status;
    private Long ownerId;

    public static ShopResponse from(Shop shop) {
        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .address(shop.getAddress())
                .status(shop.getStatus())
                .ownerId(shop.getOwner().getId())
                .build();
    }
}





