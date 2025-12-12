package com.pos.commerce.presentation.shop.dto;

import com.pos.commerce.domain.shop.Shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

/* @매장 상세정보 응답 */
@Schema(description = "매장 상세 응답")
public class DetailShopResponse {
    
    /* @매장 ID */
    @Schema(description = "매장 ID" , example = "1")
    private Long id;
    /* @매장 코드 */
    @Schema(description = "매장 코드" , example = "shop123")
    private String shopCode;
    /* @매장 이름 */
    @Schema(description = "매장 이름" , example = "매장1")
    private String shopName;

    public static DetailShopResponse from(Shop shop) {
        return DetailShopResponse.builder()
                .id(shop.getId())
                .shopCode(shop.getShopCode())
                .shopName(shop.getShopName())
                .build();
    }
}





