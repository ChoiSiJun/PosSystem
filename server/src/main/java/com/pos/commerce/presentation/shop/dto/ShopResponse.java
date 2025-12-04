package com.pos.commerce.presentation.shop.dto;

import com.pos.commerce.domain.shop.Shop;

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
    private String shopCode;
    private String password;
    private String adminPassword;

    public static ShopResponse from(Shop shop) {
        return ShopResponse.builder()
                .id(shop.getId())
                .shopCode(shop.getShopCode())
                .password(shop.getPassword())
                .adminPassword(shop.getAdminPassword())
                .build();
    }
}





