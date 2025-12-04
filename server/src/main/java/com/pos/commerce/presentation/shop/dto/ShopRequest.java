package com.pos.commerce.presentation.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopRequest {
    private String shopCode;
    private String password;
    private String adminPassword;
}





